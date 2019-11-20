package com.glitter.spring.boot.observer.sessionrenewal;

import com.glitter.spring.boot.service.ISession;
import org.springframework.context.ApplicationEvent;

public class SessionRenewalEvent extends ApplicationEvent {

    public SessionRenewalEvent(ISession session) {
        super(session);
    }

    public ISession getContent(){
        ISession session = this.getSource()==null?null:((ISession)this.getSource());
        return session;
    }

}
