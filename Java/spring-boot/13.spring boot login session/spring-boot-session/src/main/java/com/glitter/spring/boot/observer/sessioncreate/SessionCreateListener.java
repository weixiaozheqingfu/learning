package com.glitter.spring.boot.observer.sessioncreate;

import com.glitter.spring.boot.persistence.cache.ICacheKeyManager;
import com.glitter.spring.boot.persistence.cache.ICommonCache;
import com.glitter.spring.boot.service.ISession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SessionCreateListener implements ApplicationListener {

    @Autowired
    private ICommonCache commonCache;

    @Autowired
    private ICacheKeyManager cacheKeyManager;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if(applicationEvent instanceof SessionCreateEvent){
            ISession session = ((SessionCreateEvent) applicationEvent).getContent();
            commonCache.add(cacheKeyManager.getSessionKey(),session,cacheKeyManager.getSessionKeyExpireTime());
            return;
        }
    }
}
