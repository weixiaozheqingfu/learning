package com.glitter.spring.boot.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.context.ContextManager;
import com.glitter.spring.boot.context.JsessionIdCookieContext;
import com.glitter.spring.boot.persistence.cache.ICacheKeyManager;
import com.glitter.spring.boot.persistence.cache.ICommonCache;
import com.glitter.spring.boot.persistence.remote.ISsoRemote;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
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
            session.invalidate();
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-2", "您的账号已在其它地方登陆，若不是本人操作，请注意账号安全！");
            return false;
        }

        // sso全局会话续期
        OauthAccessToken oauthAccessToken = oauthAccessTokenService.getOauthAccessTokenByJsessionid(jsessionIdCookie);
        if (oauthAccessToken == null || StringUtils.isBlank(oauthAccessToken.getAccessToken())) {
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-100", "系统异常");
            return false;
        }
        ssoRemote.keepAlive(oauthAccessToken.getAccessToken());

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
