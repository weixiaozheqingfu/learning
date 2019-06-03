package com.glitter.spring.boot.service.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TimeProxyInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("执行TimeProxyInvocationHandler......");
        return null;
    }

}
