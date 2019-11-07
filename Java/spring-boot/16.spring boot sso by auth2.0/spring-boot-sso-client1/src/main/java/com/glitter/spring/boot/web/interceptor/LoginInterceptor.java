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

        // 登录状态jsessionIdCookie会话维护了一个accessToken与全局会话保持通讯
        OauthAccessToken oauthAccessTokenDb = oauthAccessTokenService.getOauthAccessTokenByJsessionid(jsessionIdCookie);
        if (oauthAccessTokenDb == null || StringUtils.isBlank(oauthAccessTokenDb.getAccessToken())) {
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-2", "系统异常");
            return false;
        }

        // 校验token是否有效，如果已经过期，或者有效期剩余小于30秒，则认为已过期，则触发refresh_token换access_token
        // 如果refresh_token换access_token失败，一般情况是refresh_token也失效，则触发当前会话注销，因为不注销也不行了。
        // 不注销后续没有办法与sso用户中心通讯了，比如续期等操作都没办法做，所以我们就注销重来重来。并且如果sso全局会话还存在的话，这个过程对用户是不需要登录的（用户会感觉怎么点一个操作突然到首页了，哈哈）。
        // 重新跳转到/login,即重定向到sso登录授权页，重新获取accessToken。
        // 调用auth后，即使sso全局会话失效
        // (理论上不可能,sso是一个体系,每个环境都配合好,是不会出现全局会话先失效的情况的,如果出现了,那一定哪个环节出现了问题,要修复。除非我们注销全局会话时只注销全局会话自己，不回调客户端),
        // 那么该客户端a也会自动触发重新登录,用户名输入用户名和密码后,会重新建立并加入一个信息的全局会话B，
        // 其他客户端b也是如此处理,则其他客户端执行到此处时,也会自动加入新的全局会话B中,并且此时用户也是免登录的。
        // 有了这个验证，其实sso注销全局会话时，只注销自己就行，不回调客户端的注销。这样客户端走到此验证时，发现验证不过，都会自行加入新的全局会话。
        // 但是给用户的体验不太好，首先用户点击a客户端某资源连接时，会收到提示【系统异常，请重试】，当用户重试时，会调转到登录页，重新做sso全局登录，
        // 当用户点击b客户端某资源时，也会提示【系统异常，请重试】，当用户重试时，会跳转到b的首页，而不是用户想访问的那个资源(跟a客户端一样,重新加入了sso新会话，由于a时已经完成了登录,b是免登的)
        // 这么做从安全性上没问题，也是完成了sso的功能，但用户体验实在是不好，所以我们还是注销全局会话时，注销调所有局部会话，如果有问题及时给出预警，有问题排查问题，最终使系统达到一个稳定的状态。
        // 相当于一个是拉的模式，一个是推的模式，我们采用推的模式。
        try {
            Map<String, String> map = ssoRemote.auth(oauthAccessTokenDb.getAccessToken());
            // 如果accessToken已经过期
            Object remainingExpirationTimeObj = map == null ? null : map.get("remaining_expiration_time");
            String remainingExpirationTimeStr = remainingExpirationTimeObj == null ? null : String.valueOf(remainingExpirationTimeObj);
            Long remainingExpirationTimeLong = remainingExpirationTimeStr == null ? null : Long.valueOf(remainingExpirationTimeStr);
            if (null == map || null == remainingExpirationTimeLong || remainingExpirationTimeLong < 30L ) {
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
                        this.response(httpServletRequest, httpServletResponse, loginUrl, "-2", "系统异常,请重试");
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
                    this.response(httpServletRequest, httpServletResponse, loginUrl, "-2", "系统异常,请重试");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            oauthAccessTokenService.deleteByJsessionid(session.getId());
            session.invalidate();
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-2", "系统异常,请重试");
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
