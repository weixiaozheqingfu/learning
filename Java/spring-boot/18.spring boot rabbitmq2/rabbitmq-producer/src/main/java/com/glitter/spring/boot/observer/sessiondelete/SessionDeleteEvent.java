package com.glitter.spring.boot.observer.sessiondelete;

import com.glitter.spring.boot.service.ISession;
import org.springframework.context.ApplicationEvent;

public class SessionDeleteEvent extends ApplicationEvent {

    public SessionDeleteEvent(ISession session) {
        super(session);
    }

    public ISession getContent(){
        ISession session = this.getSource()==null?null:((ISession)this.getSource());
        return session;
    }

}
