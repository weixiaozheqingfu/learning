package com.glitter.spring.boot.service;

import java.util.List;

public interface ISession {

    Long getCreationTime();

    String getId();

//    Long getLastAccessedTime();

    Object getAttribute(String var1);

    List<String> getAttributeNames();

    void setAttribute(String var1, Object var2);

    void removeAttribute(String var1);

    void invalidate();

}