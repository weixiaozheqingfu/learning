package com.glitter.spring.boot.observer.sessionupdate;

import com.glitter.spring.boot.service.ISession;
import org.springframework.context.ApplicationEvent;

public class SessionUpdateEvent extends ApplicationEvent {

    public SessionUpdateEvent(ISession session) {
        super(session);
    }

    public ISession getContent(){
        ISession session = this.getSource()==null?null:((ISession)this.getSource());
        return session;
    }

}
