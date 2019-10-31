package com.glitter.spring.boot.service;

public interface ISessionHandler {

    ISession getSession();

    void renewal(String sessionId);

}