package com.glitter.spring.boot.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.AccessTokenInner;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.context.AccessTokenInnerContext;
import com.glitter.spring.boot.context.ContextManager;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.persistence.cache.ICacheKeyManager;
import com.glitter.spring.boot.persistence.cache.ICommonHashCache;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
import com.glitter.spring.boot.service.ISessionHandler;
import com.glitter.spring.boot.util.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过调accessToken用资源接口拦截器
 */
public class OauthInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(OauthInterceptor.class);

    @Autowired
    IOauthAccessTokenService oauthAccessTokenService;

    @Autowired
    ICommonHashCache commonHashCache;

    @Autowired
    ICacheKeyManager cacheKeyManager;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String requestURI = httpServletRequest.getRequestURI();
        logger.info("OauthInterceptor.preHandle输入参数:requestURI:{}", requestURI);

        String access_token_header = httpServletRequest.getHeader("access_token");
        String access_token_parameter = httpServletRequest.getParameter("access_token");
        logger.info("OauthInterceptor.preHandle输入参数:access_token_header:{}",access_token_header);
        logger.info("OauthInterceptor.preHandle输入参数:access_token_parameter:{}",access_token_parameter);

        if (StringUtils.isAllBlank(access_token_header, access_token_parameter)) {
            throw new BusinessException("60031", "输入参数异常,access_token为空");
        }

        // 优先使用header参数,也可以使用参数中的,两种方式都支持
        String access_token = StringUtils.isNotBlank(access_token_header) ? access_token_header : access_token_parameter;
        logger.info("OauthInterceptor.preHandle输入参数:access_token:{}",access_token);

        AccessTokenInner accessTokenInner = oauthAccessTokenService.validateAccessToken(access_token);
        logger.info("OauthInterceptor.preHandle方法:accessTokenInner:{}", JSONObject.toJSONString(accessTokenInner));

        if (!accessTokenInner.getInterfaceUri().contains(requestURI)) {
            throw new BusinessException("60034", "接口访问异常,无接口访问权限");
        }

        // TODO 针对客户端服务器请求资源接口时，根据accessToken取到的jsessionIdByAccessToken。拦截器。
        // 需要校验客户端请求的access_token在服务器端对应的会话依然没有过期，理论上不会过期，但这里做容错处理。
        // 客户端响应到服务器端返回的错误码后，知道服务器端的会话原来已经过期，则主动触发本地会话登出和调用服务器登出接口。
        String mkey = "sessionAttr:" + GlitterConstants.SESSION_USER;
        Object userInfo = commonHashCache.getValue(cacheKeyManager.getSessionKey(accessTokenInner.getJsessionid()), mkey);
        if (null == userInfo) {
            throw new BusinessException("60035", "接口访问异常,授权中心会话已失效");
        }

        AccessTokenInnerContext.set(accessTokenInner);
        logger.info("OauthInterceptor.preHandle方法:客户端:[{}],请求[{}]接口,授权人:[{}],access_token:[{}]", accessTokenInner.getClientId(), requestURI, accessTokenInner.getUserId(),access_token);

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
