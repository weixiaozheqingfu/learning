package com.glitter.spring.boot.service;

public interface ISessionHandler {

    ISession getSession();

    ISession getSession(String jsessionid);

    void renewal(String sessionId);

}