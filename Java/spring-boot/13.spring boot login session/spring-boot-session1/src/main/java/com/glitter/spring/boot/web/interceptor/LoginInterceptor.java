package com.glitter.spring.boot.web.interceptor;

import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.context.ContextManager;
import com.glitter.spring.boot.context.JsessionIdCookieContext;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.persistence.cache.ICacheKeyManager;
import com.glitter.spring.boot.persistence.cache.ICommonCache;
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

public class LoginInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ISessionHandler sessionHandler;

    @Autowired
    ICommonCache commonCache;

    @Autowired
    ICacheKeyManager cacheKeyManager;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String jsessionIdCookie = JsessionIdCookieContext.get();
        if(StringUtils.isBlank(jsessionIdCookie)){ throw new BusinessException("-2", "用户未登陆"); }

        ISession session = sessionHandler.getSession();
        if (!session.getId().equals(jsessionIdCookie)){ throw new BusinessException("-2", "未登录或会话超时，请重新登陆。"); }

        UserInfo userInfo;
        if (null == (userInfo = (UserInfo)session.getAttribute(GlitterConstants.SESSION_USER))){ throw new BusinessException("-2", "用户未登陆"); }

        String jsessionIdEffective = commonCache.get(cacheKeyManager.getLimitMultiLoginKey(String.valueOf(userInfo.getId())));
        if(StringUtils.isBlank(jsessionIdEffective)) { throw new BusinessException("-2", "未登录或会话超时，请重新登陆。"); }

        if(!jsessionIdCookie.equals(jsessionIdEffective)){
            // 被其他端"挤掉"了,就注销当前客户端与服务器端已经失效的这个会话
            session.invalidate();
            throw new BusinessException("-2", "您的账号已在其它地方登陆，若不是本人操作，请注意账号安全！");
        }

        logger.info("LoginInterceptor.preHandle验证成功,jsessionId:{},userId:{},fullName:{}",jsessionIdCookie,userInfo.getId(),userInfo.getFullName());
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
