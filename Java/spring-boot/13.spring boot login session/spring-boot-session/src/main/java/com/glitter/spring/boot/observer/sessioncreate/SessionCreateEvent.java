package com.glitter.spring.boot.observer.sessioncreate;

import com.glitter.spring.boot.service.ISession;
import org.springframework.context.ApplicationEvent;

public class SessionCreateEvent extends ApplicationEvent {

    public SessionCreateEvent(ISession session) {
        super(session);
    }

    public ISession getContent(){
        ISession session = this.getSource()==null?null:((ISession)this.getSource());
        return session;
    }

}
