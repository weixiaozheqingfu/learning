package com.glitter.spring.boot.observer;


import com.glitter.spring.boot.service.ISession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class GlitterPublisher implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext=applicationContext;
    }

    public void publishSessionUpdateEvent(ISession session){
        SessionUpdateEvent sessionUpdateEvent = new SessionUpdateEvent(session);
        applicationContext.publishEvent(sessionUpdateEvent);
    }

}
