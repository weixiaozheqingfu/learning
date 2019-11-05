package com.glitter.spring.boot.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.bean.OauthClientInfo;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.context.ContextManager;
import com.glitter.spring.boot.context.JsessionIdCookieContext;
import com.glitter.spring.boot.persistence.cache.ICacheKeyManager;
import com.glitter.spring.boot.persistence.cache.ICommonCache;
import com.glitter.spring.boot.persistence.remote.ISsoRemote;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
import com.glitter.spring.boot.service.IOauthClientInfoService;
import com.glitter.spring.boot.service.ISession;
import com.glitter.spring.boot.service.ISessionHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ISessionHandler sessionHandler;
    @Autowired
    ICommonCache commonCache;
    @Autowired
    ICacheKeyManager cacheKeyManager;
    @Autowired
    ISsoRemote ssoRemote;
    @Autowired
    IOauthAccessTokenService oauthAccessTokenService;
    @Autowired
    private IOauthClientInfoService oauthClientInfoService;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String loginUrl = "http://localhost:8081/login";

        String jsessionIdCookie = JsessionIdCookieContext.get();
        if (StringUtils.isBlank(jsessionIdCookie)) {
            this.response(httpServletRequest, httpServletResponse, loginUrl,"-2", "用户未登陆");
            return false;
        }

        ISession session = sessionHandler.getSession();
        if (!session.getId().equals(jsessionIdCookie)) {
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-2", "未登录或会话超时，请重新登陆。");
            return false;
        }

        UserInfo userInfo;
        if (null == (userInfo = (UserInfo) session.getAttribute(GlitterConstants.SESSION_USER))) {
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-2", "用户未登陆");
            return false;
        }

        String jsessionIdEffective = commonCache.get(cacheKeyManager.getLimitMultiLoginKey(String.valueOf(userInfo.getUserId())));
        if (StringUtils.isBlank(jsessionIdEffective)) {
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-2", "未登录或会话超时，请重新登陆。");
            return false;
        }

        if (!jsessionIdCookie.equals(jsessionIdEffective)) {
            // 被其他端"挤掉"了,就注销当前客户端与服务器端已经失效的这个会话
            oauthAccessTokenService.deleteByJsessionid(session.getId());
            session.invalidate();
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-2", "您的账号已在其它地方登陆，若不是本人操作，请注意账号安全！");
            return false;
        }

        OauthAccessToken oauthAccessTokenDb = oauthAccessTokenService.getOauthAccessTokenByJsessionid(jsessionIdCookie);
        if (oauthAccessTokenDb == null || StringUtils.isBlank(oauthAccessTokenDb.getAccessToken())) {
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-100", "系统异常");
            return false;
        }

        // 校验token是否有效，如果已经过期，或者有效期剩余小于30秒，则认为已过期，则触发refresh_token换access_token
        // 如果refresh_token换access_token失败，一般情况是refresh_token也失效，则触发当前会话注销，因为不注销也不行了。
        // 不注销后续没有办法与sso用户中心通讯了，比如续期等操作都没办法做，所以我们就注销重来重来。并且如果sso全局会话还存在的话，这个过程对用户是不需要登录的（用户会感觉怎么点一个操作突然到首页了，哈哈）。
        // 重新跳转到/login,即重定向到sso登录授权页，重新获取accessToken。
        try {
            Map<String, String> map = ssoRemote.auth(oauthAccessTokenDb.getAccessToken());
            // 如果accessToken已经过期
            if (null == map || StringUtils.isBlank(map.get("remaining_expiration_time")) || Long.valueOf((String)map.get("remaining_expiration_time"))<30L ) {
                // 使用refreshToken换accessToken
                OauthClientInfo oauthClientInfo = oauthClientInfoService.getOauthClientInfoByServerType("sso");
                String client_id = oauthClientInfo.getClientId();
                try {
                    Map refreshTokenMap = ssoRemote.refreshToken(client_id, oauthAccessTokenDb.getRefreshToken(), "refresh_token");
                    String access_token = null == refreshTokenMap ? null :(String)refreshTokenMap.get("access_token");
                    String token_type = null == refreshTokenMap ? null : (String)refreshTokenMap.get("token_type");
                    String expires_in = null == refreshTokenMap ? null : (String)refreshTokenMap.get("expires_in");
                    String refresh_token = null == refreshTokenMap ? null : (String)refreshTokenMap.get("refresh_token");
                    String scope = null == refreshTokenMap ? null : (String)refreshTokenMap.get("scope");
                    Long userId = null == refreshTokenMap ? null : Long.valueOf((String)map.get("userId"));
                    // 其他字段这里不一一判断了
                    if (null == refreshTokenMap || StringUtils.isBlank(access_token)) {
                        oauthAccessTokenService.deleteByJsessionid(session.getId());
                        session.invalidate();
                        this.response(httpServletRequest, httpServletResponse, loginUrl, "-100", "系统异常,请重试");
                        return false;
                    }
                    // 更新accessToken信息
                    OauthAccessToken oauthAccessToken = new OauthAccessToken();
                    oauthAccessToken.setAccessToken(access_token);
                    oauthAccessToken.setExpireIn(StringUtils.isBlank(expires_in) ? null : Long.valueOf(expires_in));
                    oauthAccessToken.setRefreshToken(refresh_token);
                    oauthAccessTokenService.modifyById(oauthAccessToken);
                } catch (Exception e) {
                    e.printStackTrace();
                    oauthAccessTokenService.deleteByJsessionid(session.getId());
                    session.invalidate();
                    this.response(httpServletRequest, httpServletResponse, loginUrl, "-100", "系统异常,请重试");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            oauthAccessTokenService.deleteByJsessionid(session.getId());
            session.invalidate();
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-100", "系统异常,请重试");
            return false;
        }

        // sso全局会话续期
        OauthAccessToken oauthAccessTokenDb2 = oauthAccessTokenService.getOauthAccessTokenByJsessionid(jsessionIdCookie);
        ssoRemote.keepAlive(oauthAccessTokenDb2.getAccessToken());

        logger.info("LoginInterceptor.preHandle验证成功,jsessionId:{},userId:{},fullName:{}", jsessionIdCookie, userInfo.getUserId(), userInfo.getNickName());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        ContextManager.removeAllContext();
    }

    /**
     * 是否是ajax请求
     *
     * @param request
     * @return
     */
    private boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        String xRequestedWith = request.getHeader("X-Requested-With");

        boolean flag1 = accept != null && accept.indexOf("application/json") != -1;
        boolean flag2 = xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1;
        boolean isAjax = flag1 || flag2;
        return isAjax;
    }

    /**
     * 返回响应数据
     * <p>
     * 测试的话可以是postman模拟ajax,只要在postman的header头中加入X-Requested-With属性,值为XMLHttpRequest即可.
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param code
     * @param msg
     * @return
     * @throws IOException
     */
    private void response(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String loginUrl, String code, String msg) throws IOException {
        if (isAjaxRequest(httpServletRequest)) {
            httpServletRequest.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("text/html;charset=utf-8");
            ResponseResult result = new ResponseResult(code, msg);
            result.setData(loginUrl);
            httpServletResponse.getWriter().write(JSONObject.toJSONString(result));
            return;
        }
        httpServletResponse.sendRedirect(loginUrl);
        return;
    }

}
