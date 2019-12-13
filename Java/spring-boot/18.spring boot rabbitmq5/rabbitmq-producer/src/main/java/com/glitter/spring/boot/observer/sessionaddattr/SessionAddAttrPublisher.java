package com.glitter.spring.boot.observer.sessionaddattr;

import com.glitter.spring.boot.web.action.UserInfoAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class SessionAddAttrPublisher implements ApplicationContextAware,Serializable {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAction.class);

    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext=applicationContext;
    }

    public void publishEvent(SessionAddAttrParam sessionAddAttrParam){
        logger.info("SessionAddAttrPublisher.publishEvent,sessionAddAttrParam:{}", sessionAddAttrParam.getSession().getId());
        SessionAddAttrEvent sessionCreateEvent = new SessionAddAttrEvent(sessionAddAttrParam);
        applicationContext.publishEvent(sessionCreateEvent);
    }

}
