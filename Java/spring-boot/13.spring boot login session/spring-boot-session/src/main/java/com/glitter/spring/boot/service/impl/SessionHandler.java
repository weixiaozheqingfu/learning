package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.context.JsessionIdCookieContext;
import com.glitter.spring.boot.context.ResponseContext;
import com.glitter.spring.boot.observer.GlitterPublisher;
import com.glitter.spring.boot.persistence.cache.redis.SessionCacheImpl;
import com.glitter.spring.boot.service.ISession;
import com.glitter.spring.boot.service.ISessionHandler;
import com.glitter.spring.boot.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
public class SessionHandler implements ISessionHandler {

    @Autowired
    SessionCacheImpl sessionCache;

    @Autowired
    GlitterPublisher glitterPublisher;

    @Override
    public ISession getSession() {
        ISession session = new Session();
        // 如果jsessionIdCookie为空或者在服务器不存在对应的Session对象,则进行Session对象的创建和jsessionIdCookie的浏览器回写
        if (StringUtils.isBlank(JsessionIdCookieContext.get())
                //  || null == (session = (ISession)sessionCache.getSession("jsessionId..."))
                ) {
            // TODO 创建session对象并写入并生成新的jsessionIdClient到客户端浏览器 同时在redis中记录
            Cookie cookie = new Cookie(GlitterConstants.JSESSIONID, "TODO");
            cookie.setPath("/");
            CookieUtils.updateCookie(ResponseContext.get(), cookie);

            // TODO 创建Session对象，并保存在
            session = new Session();
            return session;
        }
        return session;
    }
}