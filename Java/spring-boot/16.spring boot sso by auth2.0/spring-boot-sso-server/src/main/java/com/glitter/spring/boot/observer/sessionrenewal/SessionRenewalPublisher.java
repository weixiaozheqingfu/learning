package com.glitter.spring.boot.observer.sessionrenewal;

import com.glitter.spring.boot.service.ISession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class SessionRenewalPublisher implements ApplicationContextAware,Serializable {

    private static final Logger logger = LoggerFactory.getLogger(SessionRenewalPublisher.class);

    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext=applicationContext;
    }

    public void publishEvent(ISession session){
        logger.info("SessionRenewalPublisher.publishEvent,sessionId:{}", session.getId());
        SessionRenewalEvent sessionDeleteEvent = new SessionRenewalEvent(session);
        applicationContext.publishEvent(sessionDeleteEvent);
    }

}
