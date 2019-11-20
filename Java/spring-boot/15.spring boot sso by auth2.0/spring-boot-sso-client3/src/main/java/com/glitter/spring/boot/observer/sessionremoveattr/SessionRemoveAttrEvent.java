package com.glitter.spring.boot.observer.sessionremoveattr;

import org.springframework.context.ApplicationEvent;

public class SessionRemoveAttrEvent extends ApplicationEvent {

    public SessionRemoveAttrEvent(SessionRemoveAttrParam sessionRemoveAttrParam) {
        super(sessionRemoveAttrParam);
    }

    public SessionRemoveAttrParam getContent(){
        SessionRemoveAttrParam sessionRemoveAttrParam = this.getSource()==null?null:((SessionRemoveAttrParam)this.getSource());
        return sessionRemoveAttrParam;
    }

}
