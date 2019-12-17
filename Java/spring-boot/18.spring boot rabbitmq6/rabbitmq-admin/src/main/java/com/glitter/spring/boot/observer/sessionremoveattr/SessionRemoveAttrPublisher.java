package com.glitter.spring.boot.observer.sessionremoveattr;

import com.glitter.spring.boot.web.action.UserInfoAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class SessionRemoveAttrPublisher implements ApplicationContextAware,Serializable {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAction.class);

    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext=applicationContext;
    }

    public void publishEvent(SessionRemoveAttrParam sessionRemoveAttrParam){
        logger.info("SessionRemoveAttrPublisher.publishEvent,sessionId:{}", sessionRemoveAttrParam.getSession().getId());
        SessionRemoveAttrEvent sessionRemoveEvent = new SessionRemoveAttrEvent(sessionRemoveAttrParam);
        applicationContext.publishEvent(sessionRemoveEvent);
    }

}
