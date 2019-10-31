package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.context.JsessionIdCookieContext;
import com.glitter.spring.boot.context.ResponseContext;
import com.glitter.spring.boot.observer.sessionrenewal.SessionRenewalPublisher;
import com.glitter.spring.boot.persistence.cache.ICacheKeyManager;
import com.glitter.spring.boot.persistence.cache.ICommonHashCache;
import com.glitter.spring.boot.service.ISession;
import com.glitter.spring.boot.service.ISessionHandler;
import com.glitter.spring.boot.util.CookieUtils;
import com.glitter.spring.boot.util.SpringContextUtil;
import com.glitter.spring.boot.web.action.UserInfoAction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.UUID;

@Service
public class SessionHandler implements ISessionHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAction.class);

    @Autowired
    ICommonHashCache commonHashCache;

    @Autowired
    ICacheKeyManager cacheKeyManager;


    /**
     * 整个项目中获取session对象的唯一方式
     * 并且方法内部也是整个项目中创建session的唯一地方
     *
     * 每一个线程中多处调用该方法得到的session对象都是同一个,
     * 除非某处在调用该方法时session在redis中过期或认为销毁,则该方法会返回一个全新的session对象,这是完成正常的逻辑.
     *
     * 续期只在此处即可,即每个线程在获取session对象时续期即可,对session属性的增删改就不再续期了,因为一个线程整体的请求时间
     * 本来就很短暂,如果使用到了session,就必然至少要getSession()一次,所以只在这里续期就足够了,其他session内部的增删改查频繁续期没有意义,纯属性能浪费.
     *
     * @return
     */
    @Override
    public ISession getSession() {
        ISession session = SessionContext.get();

        // 如果当前线程中的session不为空,并且在缓存中依然存在,则表明session未过期,可以继续直接使用该线程中的session
        if(null != session && commonHashCache.isExists(cacheKeyManager.getSessionKey(session.getId()))){
            commonHashCache.renewal(cacheKeyManager.getSessionKey(session.getId()), cacheKeyManager.getSessionKeyExpireTime());
            SpringContextUtil.getBean(SessionRenewalPublisher.class).publishEvent(session);
            return session;
        }

        // 某线程中第一次调用该方法时,要么执行此逻辑
        if (StringUtils.isBlank(JsessionIdCookieContext.get()) || !commonHashCache.isExists(cacheKeyManager.getSessionKey(JsessionIdCookieContext.get())) ) {
            session = new Session(UUID.randomUUID().toString());

            Cookie cookie = new Cookie(GlitterConstants.JSESSIONID, session.getId());
            cookie.setPath("/");
            CookieUtils.updateCookie(ResponseContext.get(), cookie);

            SessionContext.set(session);
            return session;
        }

        // 某线程中第一次调用该方法时,要么执行此逻辑
        session = new Session(JsessionIdCookieContext.get());
        commonHashCache.renewal(cacheKeyManager.getSessionKey(session.getId()), cacheKeyManager.getSessionKeyExpireTime());
        SpringContextUtil.getBean(SessionRenewalPublisher.class).publishEvent(session);
        SessionContext.set(session);
        return session;
    }

    @Override
    public void renewal(String sessionId) {
        if (commonHashCache.isExists(cacheKeyManager.getSessionKey(sessionId))) {
            commonHashCache.renewal(cacheKeyManager.getSessionKey(sessionId), cacheKeyManager.getSessionKeyExpireTime());
        }
    }
}