package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.context.JsessionIdRequestContext;
import com.glitter.spring.boot.context.JsessionIdResponseContext;
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

//    /**
//     * 如果jsessionIdCookie为空或者在服务器不存在对应的Session对象,则进行Session对象的创建和jsessionIdCookie的浏览器回写。
//     * 即无论客户端传入的jseesionId是否合法,这里都会保证返回一个合法合适的session会话对象。
//     *
//     * 保证一个线程中的sessionId值是一致的,但每次通过SessionHandler.getSession()拿到的对象都是最新new出来的,与redis中会话信息是同步的。
//     * 为了简单就采用这种方式,足够使用,因为一个线程中调用SessionHandler.getSession()的次数不会太多,只要拿到的session对象信息是与redis是同步的即可,session对象不是同一个无所谓.
//     * 这种感觉就好像每次都从数据库查询id为1的人员信息对象一样,每次调用数据库返回的java对象都是新的,但是java对象中的人员信息每次都是一致的(如果两次查询之间数据库数据未发生变更).
//     *
//     * 当然伪单例模式也可以,即在一个线程中,通过SessionHandler.getSession()拿到的对象都是同一个,感觉在这一个线程中拿到的session对象就好像是单例一样,挺好,只是实现起来就复杂多了,简约起见,就不再研究这种方式了.
//     *
//     * @return
//     */
//    @Override
//    public ISession getSession() {
//        ISession session = null;
//        if (StringUtils.isBlank(JsessionIdRequestContext.get()) || null == (session = commonCache.get(cacheKeyManager.getSessionKey(JsessionIdRequestContext.get()))) ) {
//
//            session = commonCache.get(cacheKeyManager.getSessionKey(JsessionIdResponseContext.get()));
//            if(null != session){
//                commonCache.renewal(cacheKeyManager.getSessionKey(session.getId()), cacheKeyManager.getSessionKeyExpireTime());
//                return session;
//            }
//
//            session = new Session();
//            commonCache.add(cacheKeyManager.getSessionKey(session.getId()), session, cacheKeyManager.getSessionKeyKeyExpireTime());
//
//            Cookie cookie = new Cookie(GlitterConstants.JSESSIONID, session.getId());
//            cookie.setPath("/");
//            CookieUtils.updateCookie(ResponseContext.get(), cookie);
//
//            JsessionIdResponseContext.set(session.getId());
//            logger.error("session创建完毕");
//            return session;
//        }
//        commonCache.renewal(cacheKeyManager.getSessionKey(session.getId()), cacheKeyManager.getSessionKeyExpireTime());
//        return session;
//    }


    /**
     * 整个项目中获取session对象的唯一方式
     * 并且方法内部也是整个项目中创建session的唯一方式
     *
     * 每一个线程中多处调用该方法得到的session对象都是同一个,
     * 除非某处在调用该方法时session在redis中过期或认为销毁,则该方法会返回一个全新的session对象,这是完成正常的逻辑.
     * @return
     */
    @Override
    public ISession getSession() {
        ISession session = SessionContext.get();

        // 如果当前线程中的session不为空,并且在缓存中依然存在,则表明session未过期,可以继续直接使用该线程中的session
        if(null != session && commonCache.isExists(cacheKeyManager.getSessionKey(session.getId()))){
            commonCache.renewal(cacheKeyManager.getSessionKey(session.getId()), cacheKeyManager.getSessionKeyExpireTime());
            return session;
        }

        // 某线程中第一次调用该方法时,要么执行此逻辑
        if (StringUtils.isBlank(JsessionIdRequestContext.get()) || null == (session = commonCache.get(cacheKeyManager.getSessionKey(JsessionIdRequestContext.get()))) ) {
            session = new Session();
            commonCache.add(cacheKeyManager.getSessionKey(session.getId()), session, cacheKeyManager.getSessionKeyKeyExpireTime());

            Cookie cookie = new Cookie(GlitterConstants.JSESSIONID, session.getId());
            cookie.setPath("/");
            CookieUtils.updateCookie(ResponseContext.get(), cookie);

            SessionContext.set(session);
            return session;
        }

        // 某线程中第一次调用该方法时,要么执行此逻辑
        commonCache.renewal(cacheKeyManager.getSessionKey(session.getId()), cacheKeyManager.getSessionKeyExpireTime());
        SessionContext.set(session);
        return session;
    }
}