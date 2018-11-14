package com.glitter.spring.boot.observer;

import com.glitter.spring.boot.persistence.cache.ISessionCache;
import com.glitter.spring.boot.service.ISession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class GlitterListener implements ApplicationListener {

    @Autowired
    private ISessionCache sessionCache;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof SessionUpdateEvent){
            ISession session = ((SessionUpdateEvent) event).getContent();
            // TODO 更新session信息

            return;
        }
    }

}
