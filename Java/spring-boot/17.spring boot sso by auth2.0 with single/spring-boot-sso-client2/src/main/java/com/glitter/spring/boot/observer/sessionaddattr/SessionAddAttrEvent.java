package com.glitter.spring.boot.observer.sessionaddattr;

import org.springframework.context.ApplicationEvent;

public class SessionAddAttrEvent extends ApplicationEvent {

    public SessionAddAttrEvent(SessionAddAttrParam sessionAddAttrParam) {
        super(sessionAddAttrParam);
    }

    public SessionAddAttrParam getContent(){
        SessionAddAttrParam sessionAddAttrParam = this.getSource()==null?null:((SessionAddAttrParam)this.getSource());
        return sessionAddAttrParam;
    }

}
