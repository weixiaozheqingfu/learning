package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.ISession;

public class SessionContext {

    protected static ThreadLocal<ISession> threadLocal = new ThreadLocal<>();

    protected static void set(ISession param) {
        threadLocal.set(param);
    }

    protected static ISession get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

}
