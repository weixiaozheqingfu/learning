package com.glitter.spring.boot.observer.sessionaddattr;

import com.glitter.spring.boot.service.ISession;
import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Data
public class SessionAddAttrParam {

    private ISession session;

    private ConcurrentMap<String, Object> attribute = new ConcurrentHashMap();

}
