package com.glitter.spring.boot.observer.sessioncreate;

import com.glitter.spring.boot.persistence.cache.ICacheKeyManager;
import com.glitter.spring.boot.persistence.cache.ICommonCache;
import com.glitter.spring.boot.service.ISession;
import com.glitter.spring.boot.web.action.UserInfoAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SessionCreateListener implements ApplicationListener<SessionCreateEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAction.class);

    @Autowired
    private ICommonCache commonCache;

    @Autowired
    private ICacheKeyManager cacheKeyManager;

    @Override
    public void onApplicationEvent(SessionCreateEvent applicationEvent) {
        logger.error("onApplicationEvent......");
        ISession session = applicationEvent.getContent();
        commonCache.add(cacheKeyManager.getSessionKey(session.getId()),session,cacheKeyManager.getSessionKeyExpireTime());
        return;
    }

}
