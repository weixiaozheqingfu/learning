package com.glitter.spring.boot.observer.sessiondelete;

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
public class SessionDeleteListener implements ApplicationListener<SessionDeleteEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAction.class);

    @Autowired
    private ICommonCache commonCache;

    @Autowired
    private ICacheKeyManager cacheKeyManager;

    @Override
    public void onApplicationEvent(SessionDeleteEvent applicationEvent) {
        logger.info("SessionDeleteListener.onApplicationEvent,输入参数:{}", applicationEvent);
        ISession session = applicationEvent.getContent();
        logger.info("SessionDeleteListener.onApplicationEvent,sessionId:{}", session.getId());
        commonCache.del(cacheKeyManager.getSessionKey(session.getId()));
        return;
    }

}
