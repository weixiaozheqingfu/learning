package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.observer.sessioncreate.SessionCreatePublisher;
import com.glitter.spring.boot.service.ISession;
import com.glitter.spring.boot.util.SpringContextUtil;
import com.glitter.spring.boot.web.action.UserInfoAction;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Long creationTime;
    private Long lastAccessedTime;

    protected Session(){
        Long now = System.currentTimeMillis();
        this.id = UUID.randomUUID().toString();
        this.attributes = new ConcurrentHashMap();
        this.creationTime = now;
        this.lastAccessedTime = now;
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

    public void setLastAccessedTime(Long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    @Override
    public Long getCreationTime() {
        return this.getCreationTime();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Long getLastAccessedTime() {
        return null;
    }

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
        logger.error("时间发布完毕!");
    }

    @Override
    public void removeAttribute(String key) {
        this.attributes.remove(key);
        SpringContextUtil.getBean(SessionCreatePublisher.class).publishEvent(this);
    }

    @Override
    public void invalidate() {
        // TODO redis清除,同时销毁当前对象
    }
}