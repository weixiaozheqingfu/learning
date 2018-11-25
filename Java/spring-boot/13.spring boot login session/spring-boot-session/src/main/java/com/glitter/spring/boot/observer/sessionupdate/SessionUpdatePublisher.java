package com.glitter.spring.boot.observer.sessionupdate;

import com.glitter.spring.boot.service.ISession;
import com.glitter.spring.boot.web.action.UserInfoAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class SessionUpdatePublisher implements ApplicationContextAware,Serializable {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAction.class);

    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext=applicationContext;
    }

    public void publishEvent(ISession session){
        logger.info("SessionUpdatePublisher.publishEvent,sessionId:{}", session.getId());
        SessionUpdateEvent sessionCreateEvent = new SessionUpdateEvent(session);
        applicationContext.publishEvent(sessionCreateEvent);
    }

}
