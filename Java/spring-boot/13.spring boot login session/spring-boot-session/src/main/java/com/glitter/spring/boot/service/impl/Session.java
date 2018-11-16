package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.observer.sessioncreate.SessionCreatePublisher;
import com.glitter.spring.boot.service.ISession;
import com.glitter.spring.boot.util.SpringContextUtil;
import com.glitter.spring.boot.web.action.UserInfoAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;


public class Session extends HashMap implements ISession {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAction.class);

    SessionCreatePublisher sessionCreatePublisher = SpringContextUtil.getBean(SessionCreatePublisher.class);

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
        super.put(key, value);
        sessionCreatePublisher.publishEvent(this);
         logger.error("时间发布完毕!");
    }

    @Override
    public void removeAttribute(String key) {
        super.remove(key);
        sessionCreatePublisher.publishEvent(this);
    }

    @Override
    public void invalidate() {

    }
}