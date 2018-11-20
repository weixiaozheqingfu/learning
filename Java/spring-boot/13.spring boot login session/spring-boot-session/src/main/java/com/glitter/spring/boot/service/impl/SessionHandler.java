package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.context.JsessionIdCookieContext;
import com.glitter.spring.boot.context.RequestContext;
import com.glitter.spring.boot.context.ResponseContext;
import com.glitter.spring.boot.persistence.cache.ICacheKeyManager;
import com.glitter.spring.boot.persistence.cache.ICommonCache;
import com.glitter.spring.boot.service.ISession;
import com.glitter.spring.boot.service.ISessionHandler;
import com.glitter.spring.boot.util.CookieUtils;
import com.glitter.spring.boot.web.action.UserInfoAction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
public class SessionHandler implements ISessionHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAction.class);

    @Autowired
    ICommonCache commonCache;

    @Autowired
    ICacheKeyManager cacheKeyManager;

    /**
     * 如果jsessionIdCookie为空或者在服务器不存在对应的Session对象,则进行Session对象的创建和jsessionIdCookie的浏览器回写。
     * 即无论客户端传入的jseesionId是否合法,这里都会保证返回一个合法合适的session会话对象。
     * @return
     */
    @Override
    public ISession getSession() {
        ISession session = null;
        if (StringUtils.isBlank(JsessionIdCookieContext.get()) || null == (session = commonCache.get(cacheKeyManager.getSessionKey(JsessionIdCookieContext.get()))) ) {
            session = new Session();
            commonCache.add(cacheKeyManager.getSessionKey(session.getId()), session, cacheKeyManager.getSessionKeyKeyExpireTime());

            Cookie cookie = CookieUtils.getCookieByName(RequestContext.get(),GlitterConstants.JSESSIONID);
            if(null == cookie){
                cookie = new Cookie(GlitterConstants.JSESSIONID, session.getId());
                cookie.setPath("/");
                CookieUtils.updateCookie(ResponseContext.get(), cookie);
            }else{
                cookie.setValue(session.getId());
            }

            logger.error("session创建完毕");
            return session;
        }
        commonCache.renewal(cacheKeyManager.getSessionKey(session.getId()), cacheKeyManager.getSessionKeyExpireTime());
        return session;
    }
}