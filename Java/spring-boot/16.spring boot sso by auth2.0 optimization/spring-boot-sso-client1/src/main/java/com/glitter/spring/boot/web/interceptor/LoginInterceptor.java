package com.glitter.spring.boot.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.bean.OauthClientInfo;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.context.ContextManager;
import com.glitter.spring.boot.context.JsessionIdCookieContext;
import com.glitter.spring.boot.exception.BusinessException;
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
        String loginUrl = GlitterConstants.DOMAIN_SSO_CLIETN1 +"/login";

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

        // 登录状态jsessionIdCookie会话维护了一个accessToken与全局会话保持通讯
        OauthAccessToken oauthAccessTokenDb = oauthAccessTokenService.getOauthAccessTokenByJsessionid(jsessionIdCookie);
        if (oauthAccessTokenDb == null || StringUtils.isBlank(oauthAccessTokenDb.getAccessToken())) {
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-2", "系统异常");
            return false;
        }

        if (!jsessionIdCookie.equals(jsessionIdEffective)) {
            // 被其他端"挤掉"了,注销局部会话。
            oauthAccessTokenService.deleteByJsessionid(session.getId());
            session.invalidate();
            // 注销全局会话。因为如果不注销这端的单点登录整体会话,那么用户收到下面的提示后，点击这段任何客户端的任何功能都会在后台再次默默完成登录，再把单端会话抢过来。
            // 这体验就不太好了，相当于强制把另一端的登录给踢掉了，而这个操作应该留给用户来做才好。
            // 所以既然登录被其他端踢掉了，那咱这端就整体注销，如果用户要这端登录，那就自行在这端完成登录挤掉那一端整体会话即可。
            ssoRemote.logout(oauthAccessTokenDb.getAccessToken());
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-2", "您的账号已在其它地方登陆，若不是本人操作，请注意账号安全！");
            return false;
        }

        // 校验access_token是否有效，只要返回值不是成功0,就认为是失效如果已经过期，或者有效期剩余小于30秒，则认为已过期，则触发refresh_token换access_token过程。
        // 如果refresh_token换access_token失败，一般情况是refresh_token也失效，则触发注销局部会话，并重定向到sso登录页重新走登录流程。
        // 因为不注销也不行了。不注销后续没有办法与sso用户中心通讯了，比如续期等操作都没办法做。
        // 如果sso全局会话还存在的话,这个过程对用户是不需要输入用户名密码的（但是用户会感觉明明点一个操作到某页，但怎么突然到首页了，哈哈）,这根绳上换新的access_token和jsessionid_client这两个蚂蚱。
        // 如果sso全局会话不存在的话,则当前客户端为全新sso整体会话建立的第一个客户端,在全新的这个整体会话中,维系了一根新绳子和新蚂蚱。

        // 调用auth后，即使sso全局会话失效(比如操作其他客户单退出登录操作了)
        // 那么该客户端a也会自动触发重新登录,用户名输入用户名和密码后,会重新建立并加入一个全新的全局会话B，
        // 其他客户端b也是如此处理,则其他客户端执行到此处时,也会自动加入新的全局会话B中,并且此时用户也是免登录的。
        // 有了这个验证，其实sso注销全局会话时，只注销自己就行，不回调客户端的注销。这样客户端走到此验证时，发现验证不过，都会自行加入新的全局会话。
        // 但是给用户的体验不太好，首先用户点击a客户端某资源连接时，会收到提示【系统异常，请重试】，当用户重试时，会调转到登录页，重新做sso全局登录，
        // 当用户点击b客户端某资源时，也会提示【系统异常，请重试】，当用户重试时，会跳转到b的首页，而不是用户想访问的那个资源(跟a客户端一样,重新加入了sso新会话，由于a时已经完成了登录,b是免登的)
        // 这么做从安全性上没问题，也是完成了sso的功能，但用户体验实在是不好，所以我们还是注销全局会话时，注销调所有局部会话，如果有问题及时给出预警，有问题排查问题，最终使系统达到一个稳定的状态。
        // 相当于一个是拉的模式，一个是推的模式，我们采用推的模式。

        ResponseResult<Map> responseResult = null;
        Map<String, String> map = null;
        Long remainingExpirationTimeLong = null;
        try {
            responseResult = ssoRemote.auth(oauthAccessTokenDb.getAccessToken());
            map = responseResult.getData();
            if (responseResult.getCode().equals("0")) {
                // TODO
            } else {
                // TODO 过期的情况,需要换accessToken。
            }

            // accessToken有效的情况下取出remaining_expiration_time
            Object remainingExpirationTimeObj = map == null ? null : map.get("remaining_expiration_time");
            String remainingExpirationTimeStr = remainingExpirationTimeObj == null ? null : String.valueOf(remainingExpirationTimeObj);
            remainingExpirationTimeLong = remainingExpirationTimeStr == null ? null : Long.valueOf(remainingExpirationTimeStr);
            // 如果accessToken有效的情况下取出remaining_expiration_time为空,则是系统对接的异常情况,要做邮件预警,并线下反馈,排查解决sso_server项目的问题。
            if (null == map || null == remainingExpirationTimeLong) {
                throw new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统调用异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            oauthAccessTokenService.deleteByJsessionid(session.getId());
            session.invalidate();
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-2", "系统异常,请重试");
            return false;
        }

        // 如果accessToken未过期但是
        if (remainingExpirationTimeLong < 30L ) {
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
