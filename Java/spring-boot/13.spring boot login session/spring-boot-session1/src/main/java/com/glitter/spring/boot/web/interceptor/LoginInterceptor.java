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
        // 如果客户端请求的jsessionIdCookie值为空,可能是第一次访问或者手动清空cookie,或者cookie到期等各种情况,
        // 不论哪种情况,没说的,当前请求发出的客户端浏览器,没有相应的服务器会话对象数据与之对应,所以用户在当前客户端就是未登录状态。
        if(StringUtils.isBlank(jsessionIdCookie)){ throw new BusinessException("-2", "用户未登陆"); }

        // 如果客户端请求的jsessionIdCookie值在服务器没有端匹配到相应的session会话对象,
        // 比如退出时直接调用了session.invalidate()方法,服务器端会删除jsessionIdCookie对应的session会话对象,当客户端浏览器下次还用jsessionIdCookie请求时,自然是找不到对应的服务器session会话信息的,
        // 那么用户在当前客户端就是未登录状态。
        ISession session = sessionHandler.getSession();
        if (!session.getId().equals(jsessionIdCookie)){ throw new BusinessException("-2", "未登录或会话超时，请重新登陆。"); }

        // 如果客户端请求的jsessionIdCookie值在服务器端匹配到了相应的session会话对象,但是session会话对象中的用户信息是空,
        // 比如用户在客户端浏览器调用了登陆页面,登陆也有请求验证码的功能,这时候广义session会话就已经建立了,jsessionIdCookie有与之对应的session对象与之对应,
        // 又比如退出时只调用了session.removeAttribute(GlitterConstants.SESSION_USER);那么在服务器端的session会话对象中就清除了标识用户登录的属性信息,
        // 那么用户在当前客户端也是未登录状态。
        UserInfo userInfo;
        if (null == (userInfo = (UserInfo)session.getAttribute(GlitterConstants.SESSION_USER))){ throw new BusinessException("-2", "用户未登陆"); }

        // 代码至此说明用户当前所使用的客户端浏览器与服务器端存在会话并且处于登陆状态,只是不知道该登录状态是否被其他客户端"挤掉",也就是说虽然有会话并且是登陆的,但是如果被其他端"挤掉",这里就认为是无效的。
        // 怎么认为无效,就是下面的登陆时的逻辑代码加上下面的逻辑代码做到的。
        // 限制多端同时登陆
        String jsessionIdEffective = commonCache.get(cacheKeyManager.getLimitMultiLoginKey(String.valueOf(userInfo.getId())));
        // 正常情况不可能为空,即使发生意外情况为空,安全起见,要求重新登陆.
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
