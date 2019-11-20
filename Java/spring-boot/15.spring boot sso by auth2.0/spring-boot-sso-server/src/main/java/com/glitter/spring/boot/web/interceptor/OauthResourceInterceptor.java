package com.glitter.spring.boot.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.AccessTokenInParam;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.context.AccessTokenInnerContext;
import com.glitter.spring.boot.context.ContextManager;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
import com.glitter.spring.boot.service.ISessionHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通过调accessToken用资源接口拦截器
 */
public class OauthResourceInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(OauthResourceInterceptor.class);

    @Autowired
    IOauthAccessTokenService oauthAccessTokenService;
    @Autowired
    public ISessionHandler sessionHandler;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String requestURI = httpServletRequest.getRequestURI();
        logger.info("OauthInterceptor.preHandle输入参数:requestURI:{}", requestURI);

        String access_token_header = httpServletRequest.getHeader("access_token");
        String access_token_parameter = httpServletRequest.getParameter("access_token");
        logger.info("OauthInterceptor.preHandle输入参数:access_token_header:{}", access_token_header);
        logger.info("OauthInterceptor.preHandle输入参数:access_token_parameter:{}", access_token_parameter);

        if (StringUtils.isAllBlank(access_token_header, access_token_parameter)) {
            throw new BusinessException("60031", "输入参数异常,access_token为空");
        }

        // 优先使用header参数,也可以使用参数中的,两种方式都支持
        String access_token = StringUtils.isNotBlank(access_token_header) ? access_token_header : access_token_parameter;
        logger.info("OauthInterceptor.preHandle输入参数:access_token:{}", access_token);

        // 验证access_token是否有效，并获取accessToken对应的相关信息
        AccessTokenInParam accessTokenInParam = oauthAccessTokenService.validateAccessToken(access_token);
        logger.info("OauthInterceptor.preHandle方法:accessTokenInner:{}", JSONObject.toJSONString(accessTokenInParam));

        // 验证用户访问的接口是否在accessToken的授权范围内
        if (!accessTokenInParam.getInterfaceUri().contains(requestURI)) {
            throw new BusinessException("60034", "接口访问异常,无接口访问权限");
        }

        // 验证access_token对应的jsessionid全局会话是否仍在会话期间内
        UserInfo userinfo = (UserInfo) sessionHandler.getSession(accessTokenInParam.getJsessionid()).getAttribute(GlitterConstants.SESSION_USER);
        if (userinfo == null) {
            throw new BusinessException("60032", "sso全局会话已过期，请重新登录");
        }

        // 为全局会话续期
        sessionHandler.renewal(accessTokenInParam.getJsessionid());

        AccessTokenInnerContext.set(accessTokenInParam);
        logger.info("OauthInterceptor.preHandle方法:客户端:[{}],请求[{}]接口,授权人:[{}],access_token:[{}]", accessTokenInParam.getClientId(), requestURI, accessTokenInParam.getUserId(), access_token);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        ContextManager.removeAllContext();
    }

}
