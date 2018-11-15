package com.glitter.spring.boot.observer.sessioncreate;

import com.glitter.spring.boot.service.ISession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SessionCreatePublisher implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext=applicationContext;
    }

    public void publishEvent(ISession session){
        SessionCreateEvent sessionCreateEvent = new SessionCreateEvent(session);
        applicationContext.publishEvent(sessionCreateEvent);
    }

}
