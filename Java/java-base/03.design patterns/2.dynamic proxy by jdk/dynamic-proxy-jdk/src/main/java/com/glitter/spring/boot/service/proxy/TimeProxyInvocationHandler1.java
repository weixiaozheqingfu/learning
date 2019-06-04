package com.glitter.spring.boot.service.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TimeProxyInvocationHandler1 implements InvocationHandler {

    private Object target;

    public TimeProxyInvocationHandler1(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Long beginTime = System.currentTimeMillis();
        System.out.println("TimeProxyInvocationHandler开始执行,开始执行时间:"+beginTime);

        Object o = method.invoke(target, args);

        Long endTime = System.currentTimeMillis();
        System.out.println("TimeProxyInvocationHandler执行完毕,执行完毕时间:"+endTime);
        System.out.println("方法执行耗时:" + (endTime - beginTime) + "毫秒");

        return o;
    }

}
