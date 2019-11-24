package com.glitter.spring.boot.observer.sessionremoveattr;

import com.glitter.spring.boot.service.ISession;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class SessionRemoveAttrParam {

    private ISession session;

    private Map<String, Object> attribute;

    public SessionRemoveAttrParam(){
        this.attribute = new HashMap();
    }

}
