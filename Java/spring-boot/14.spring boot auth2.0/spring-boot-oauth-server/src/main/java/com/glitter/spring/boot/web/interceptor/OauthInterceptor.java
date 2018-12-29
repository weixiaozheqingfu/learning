package com.glitter.spring.boot.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.AccessTokenInner;
import com.glitter.spring.boot.context.AccessTokenInnerContext;
import com.glitter.spring.boot.context.ContextManager;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OauthInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(OauthInterceptor.class);

    @Autowired
    IOauthAccessTokenService oauthAccessTokenService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String requestURI = httpServletRequest.getRequestURI();
        logger.info("OauthInterceptor.preHandle输入参数:requestURI:{}", requestURI);

        String access_token_header = httpServletRequest.getHeader("access_token");
        String openid_header = httpServletRequest.getHeader("openid");
        String access_token_parameter = httpServletRequest.getParameter("access_token");
        String openid_parameter = httpServletRequest.getParameter("openid");
        logger.info("OauthInterceptor.preHandle输入参数:access_token_header:{}",access_token_header);
        logger.info("OauthInterceptor.preHandle输入参数:openid_header:{}",openid_header);
        logger.info("OauthInterceptor.preHandle输入参数:access_token_parameter:{}",access_token_parameter);
        logger.info("OauthInterceptor.preHandle输入参数:openid_parameter:{}",openid_parameter);

        if (StringUtils.isAllBlank(access_token_header, access_token_parameter)) {
            throw new BusinessException("60031", "输入参数异常,access_token为空");
        }

        if (StringUtils.isAllBlank(openid_header, openid_parameter)) {
            throw new BusinessException("60032", "输入参数异常,openid为空");
        }

        // 优先使用header参数,也可以使用参数中的,两种方式都支持
        String access_token = StringUtils.isNotBlank(access_token_header) ? access_token_header : access_token_parameter;
        String openid = StringUtils.isNotBlank(openid_header) ? openid_header : openid_parameter;
        logger.info("OauthInterceptor.preHandle输入参数:access_token:{}",access_token);
        logger.info("OauthInterceptor.preHandle输入参数:openid:{}",openid);

        AccessTokenInner accessTokenInner = oauthAccessTokenService.validateAccessToken(access_token);
        logger.info("OauthInterceptor.preHandle方法:accessTokenInner:{}", JSONObject.toJSONString(accessTokenInner));

        // 如果openid是非必传项的话,其实接口就直接使用accessTokenInner.getOpenId()即可,就看如何设计了,本例是需要传openid的,那就要做校验
        if (!openid.equals(accessTokenInner.getOpenId())) {
            throw new BusinessException("60033", "输入参数异常,openid非法");
        }

        if (!accessTokenInner.getInterfaceUri().contains(requestURI)) {
            throw new BusinessException("60034", "接口访问异常,无接口访问权限");
        }

        // 如果openid是非必传项的话,资源接口就支持从AccessTokenInnerContext中取openid和userid
        AccessTokenInnerContext.set(accessTokenInner);
        logger.info("OauthInterceptor.preHandle方法:客户端:[{}],请求[{}]接口,授权人:[{}],access_token:[{}]", accessTokenInner.getClientId(), requestURI, accessTokenInner.getOpenId(),access_token);

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
