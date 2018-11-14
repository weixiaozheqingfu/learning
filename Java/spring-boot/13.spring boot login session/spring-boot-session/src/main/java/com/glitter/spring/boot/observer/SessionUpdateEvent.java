package com.glitter.spring.boot.observer;

import com.glitter.spring.boot.service.ISession;
import org.springframework.context.ApplicationEvent;

/**
 * Session属性更新事件
 */
public class SessionUpdateEvent extends ApplicationEvent {

    public SessionUpdateEvent(ISession content) {
        super(content);
    }

    public ISession getContent(){
        ISession session = this.getSource()==null?null:((ISession)this.getSource());
        return session;
    }

}
