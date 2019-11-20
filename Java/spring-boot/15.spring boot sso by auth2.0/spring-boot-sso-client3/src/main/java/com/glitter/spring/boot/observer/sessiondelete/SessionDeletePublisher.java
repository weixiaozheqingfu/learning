package com.glitter.spring.boot.observer.sessiondelete;

import com.glitter.spring.boot.service.ISession;
import com.glitter.spring.boot.web.action.UserInfoAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class SessionDeletePublisher implements ApplicationContextAware,Serializable {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAction.class);

    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext=applicationContext;
    }

    public void publishEvent(ISession session){
        logger.info("SessionDeletePublisher.publishEvent,sessionId:{}", session.getId());
        SessionDeleteEvent sessionDeleteEvent = new SessionDeleteEvent(session);
        applicationContext.publishEvent(sessionDeleteEvent);
    }

}
