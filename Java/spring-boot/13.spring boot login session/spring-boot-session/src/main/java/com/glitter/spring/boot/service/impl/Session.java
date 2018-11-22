package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.context.ResponseContext;
import com.glitter.spring.boot.observer.sessioncreate.SessionCreatePublisher;
import com.glitter.spring.boot.observer.sessiondelete.SessionDeletePublisher;
import com.glitter.spring.boot.service.ISession;
import com.glitter.spring.boot.util.CookieUtils;
import com.glitter.spring.boot.util.SpringContextUtil;
import com.glitter.spring.boot.web.action.UserInfoAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class Session implements ISession,Serializable {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAction.class);

    private String id;
    private ConcurrentMap<String, Object> attributes;
    private List<String> attributeNames;
    private Long creationTime;
//    private Long lastAccessedTime;

    protected Session(){
        Long now = System.currentTimeMillis();
        this.id = UUID.randomUUID().toString();
        this.attributes = new ConcurrentHashMap();
        this.creationTime = now;
//        this.lastAccessedTime = now;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ConcurrentMap<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(ConcurrentMap<String, Object> attributes) {
        this.attributes = attributes;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

//    public void setLastAccessedTime(Long lastAccessedTime) {
//        this.lastAccessedTime = lastAccessedTime;
//    }

    public void getAttributeNames(List<String> attributeNames) {
        this.attributeNames = attributeNames;
    }

    @Override
    public Long getCreationTime() {
        return this.creationTime;
    }

    @Override
    public String getId() {
        return this.id;
    }

//    @Override
//    public Long getLastAccessedTime() {
//        return this.lastAccessedTime;
//    }

    @Override
    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }

    @Override
    public List<String> getAttributeNames() {
        List<String> result = null;
        if (null == this.getAttributes() || this.getAttributes().size() <= 0) { return result; }
        result = new ArrayList<>();
        for (Map.Entry<String, Object> entry : this.getAttributes().entrySet()) {
            result.add(entry.getKey());
        }
        return result;
    }

    @Override
    public void setAttribute(String key, Object value) {
        this.attributes.put(key, value);
        SpringContextUtil.getBean(SessionCreatePublisher.class).publishEvent(this);
    }

    @Override
    public void removeAttribute(String key) {
        this.attributes.remove(key);
        SpringContextUtil.getBean(SessionCreatePublisher.class).publishEvent(this);
    }

    @Override
    public void invalidate() {
        SpringContextUtil.getBean(SessionDeletePublisher.class).publishEvent(this);

        // 2.创建新会话并写入浏览器,不在推荐这样做了,浪费服务器资源,并且我们重新生成会话对象也不回写覆盖新的jsessionId值到客户端,
        // 客户端下次请求时,还是使用旧的jsessionId值,服务器端发现没有对应的session对象时,自然就会重新创建一个新的出来,什么时候用,什么时候创建,这样不浪费占用服务器资源,同时也符合单一入口原则。
//        ISession session = new Session();
//        commonCache.add(cacheKeyManager.getSessionKey(session.getId()), session, cacheKeyManager.getSessionKeyKeyExpireTime());
//
//        Cookie cookie = new Cookie(GlitterConstants.JSESSIONID, session.getId());
//        cookie.setPath("/");
//        CookieUtils.updateCookie(ResponseContext.get(), cookie);
    }
}