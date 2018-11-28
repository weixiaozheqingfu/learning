package com.glitter.spring.boot.observer.sessionaddattr;

import com.glitter.spring.boot.service.ISession;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class SessionAddAttrParam {

    private ISession session;

    private Map<String, Object> attribute;

    public SessionAddAttrParam(){
        this.attribute = new HashMap();
    }

}
