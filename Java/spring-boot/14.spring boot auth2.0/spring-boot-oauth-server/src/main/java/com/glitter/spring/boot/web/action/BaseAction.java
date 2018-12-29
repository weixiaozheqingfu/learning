package com.glitter.spring.boot.web.action;


import com.glitter.spring.boot.persistence.cache.ICacheKeyManager;
import com.glitter.spring.boot.persistence.cache.ICommonCache;
import com.glitter.spring.boot.service.ISessionHandler;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseAction {

    @Autowired
    public ISessionHandler sessionHandler;

    /**
     * 限制单端登陆时,会直接使用到该对象进行操作
     * 或者直接走事件发布,因为单端登陆是独立于Session对象之外的一件事情,可以单独再controller层发布其自己对应的事件
     */
    @Autowired
    public ICommonCache commonCache;

    @Autowired
    public ICacheKeyManager cacheKeyManager;

}
