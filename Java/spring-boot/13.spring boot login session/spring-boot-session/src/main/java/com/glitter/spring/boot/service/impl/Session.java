package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.observer.GlitterPublisher;
import com.glitter.spring.boot.persistence.dao.IUserInfoDao;
import com.glitter.spring.boot.service.ISession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class Session implements ISession {

    @Autowired
    GlitterPublisher glitterPublisher;

    @Autowired
    IUserInfoDao userInfoDao;

    @Override
    public Long getCreationTime() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Long getLastAccessedTime() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int var1) {

    }

    @Override
    public Integer getMaxInactiveInterval() {
        return null;
    }

    @Override
    public Object getAttribute(String var1) {
        return null;
    }

    @Override
    public List<String> getAttributeNames() {
        return null;
    }

    @Override
    public void setAttribute(String key, Object value) {
        System.out.print("1");
//        super.put(key, value);
//        glitterPublisher.publishSessionUpdateEvent(this);
    }

    @Override
    public void removeAttribute(String key) {
//        super.remove(key);
        glitterPublisher.publishSessionUpdateEvent(this);
    }

    @Override
    public void invalidate() {

    }
}