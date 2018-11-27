package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.observer.sessionaddattr.SessionAddAttrParam;
import com.glitter.spring.boot.observer.sessionaddattr.SessionAddAttrPublisher;
import com.glitter.spring.boot.observer.sessioncreate.SessionCreatePublisher;
import com.glitter.spring.boot.observer.sessiondelete.SessionDeletePublisher;
import com.glitter.spring.boot.observer.sessionremoveattr.SessionRemoveAttrParam;
import com.glitter.spring.boot.observer.sessionremoveattr.SessionRemoveAttrPublisher;
import com.glitter.spring.boot.persistence.cache.ICacheKeyManager;
import com.glitter.spring.boot.persistence.cache.ICommonHashCache;
import com.glitter.spring.boot.service.ISession;
import com.glitter.spring.boot.util.SpringContextUtil;
import com.glitter.spring.boot.web.action.UserInfoAction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Session implements ISession,Serializable {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAction.class);

    private String id;
    private ConcurrentMap<String, Object> attributes;
    private Long creationTime;

    protected Session(){

    }

    protected Session(String id){
        Long now = System.currentTimeMillis();
        this.id = id;
        this.attributes = new ConcurrentHashMap();
        this.creationTime = now;

        Map<Object, Object> map = new HashMap();
        map.put("id", id);
        map.put("creationTime", creationTime);
        SpringContextUtil.getBean(ICommonHashCache.class).putAll(SpringContextUtil.getBean(ICacheKeyManager.class).getSessionKey(this.id), map);
        SpringContextUtil.getBean(ICommonHashCache.class).renewal(SpringContextUtil.getBean(ICacheKeyManager.class).getSessionKey(this.id), SpringContextUtil.getBean(ICacheKeyManager.class).getSessionKeyExpireTime());
        SpringContextUtil.getBean(SessionCreatePublisher.class).publishEvent(this);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Long getCreationTime() {
        if (null != this.creationTime) {
            return this.creationTime;
        }
        Long result = SpringContextUtil.getBean(ICommonHashCache.class).getValue(SpringContextUtil.getBean(ICacheKeyManager.class).getSessionKey(this.id),"creationTime");
        if (null != result) {
            this.creationTime = result;
        }
        return result;
    }

    @Override
    public Object getAttribute(String key) {
        Object result = null;
        if(StringUtils.isBlank(key)) { return result; }

        String mkey = "sessionAttr:" + key;
        if (null != this.attributes.get(key)) {
            return this.attributes.get(key);
        }

        result = SpringContextUtil.getBean(ICommonHashCache.class).getValue(SpringContextUtil.getBean(ICacheKeyManager.class).getSessionKey(this.id), mkey);
        if (null != result) {
            this.attributes.put(key, result);
        }
        return result;
    }

    @Override
    public List<String> getAttributeNames() {
        List<String> result = null;
        List<Object> keys = SpringContextUtil.getBean(ICommonHashCache.class).getKeys(SpringContextUtil.getBean(ICacheKeyManager.class).getSessionKey(this.id));
        if (null == keys || keys.size() <= 0) {
            return result;
        }

        result = new ArrayList<>();
        for (Object o : keys) {
            if (null == o || StringUtils.isBlank(o.toString())) {
                continue;
            }
            if (o.toString().startsWith("sessionAttr:")) {
                result.add(o.toString().substring(12));
            }
        }
        return result;
    }

    @Override
    public void setAttribute(String key, Object value) {
        if(StringUtils.isBlank(key)){ return; }

        String mkey = "sessionAttr:" + key;
        this.attributes.put(key, value);
        SpringContextUtil.getBean(ICommonHashCache.class).put(SpringContextUtil.getBean(ICacheKeyManager.class).getSessionKey(this.id), mkey, value);

        // 发布session属性增加事件
        SessionAddAttrParam sessionAddAttrParam = new SessionAddAttrParam();
        sessionAddAttrParam.setSession(this);
        sessionAddAttrParam.getAttribute().put(key, value);
        SpringContextUtil.getBean(SessionAddAttrPublisher.class).publishEvent(sessionAddAttrParam);
    }

    @Override
    public void removeAttribute(String key) {
        if(StringUtils.isBlank(key)){ return; }

        Object value = this.getAttribute(key);

        String mkey = "sessionAttr:" + key;
        this.attributes.remove(key);
        SpringContextUtil.getBean(ICommonHashCache.class).del(SpringContextUtil.getBean(ICacheKeyManager.class).getSessionKey(this.id), mkey);

        // 发布session属性移除事件
        SessionRemoveAttrParam sessionRemoveAttrParam = new SessionRemoveAttrParam();
        sessionRemoveAttrParam.setSession(this);
        sessionRemoveAttrParam.getAttribute().put(key, value);
        SpringContextUtil.getBean(SessionRemoveAttrPublisher.class).publishEvent(sessionRemoveAttrParam);
    }

    @Override
    public void invalidate() {
        ISession session = SessionContext.get();
        if(null != session ){
            if(this == session){
                SessionContext.remove();
            }
        }
        // 如果用户登录了,那么在发布事件时,保证this对象中最关键的用户信息存在
        List<String> attributeNames = this.getAttributeNames();
        if (null != attributeNames && attributeNames.size() > 0) {
            for (int i = 0; i < attributeNames.size() ; i++) {
                this.getAttribute(attributeNames.get(i));
            }
        }

        SpringContextUtil.getBean(ICommonHashCache.class).delAll(SpringContextUtil.getBean(ICacheKeyManager.class).getSessionKey(this.id));

        // 发布session失效事件
        SpringContextUtil.getBean(SessionDeletePublisher.class).publishEvent(this);
    }

}