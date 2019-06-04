package com.glitter.spring.boot.service.proxy;

import com.glitter.spring.boot.service.impl.WeChatPayServiceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TimeProxyInvocationHandler2 implements InvocationHandler {

    private Object target;

    public TimeProxyInvocationHandler2(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (target instanceof WeChatPayServiceImpl) {
            if (((WeChatPayServiceImpl) target).isFlag()) {
                System.out.println("TimeProxyInvocationHandler.flag:" + ((WeChatPayServiceImpl) target).isFlag());
            }else {
                System.out.println("TimeProxyInvocationHandler.flag:" + ((WeChatPayServiceImpl) target).isFlag());
            }
        }

        Long beginTime = System.currentTimeMillis();
        System.out.println("TimeProxyInvocationHandler开始执行,开始执行时间:"+beginTime);

        Object o = method.invoke(target, args);

        Long endTime = System.currentTimeMillis();
        System.out.println("TimeProxyInvocationHandler执行完毕,执行完毕时间:"+endTime);
        System.out.println("方法执行耗时:" + (endTime - beginTime) + "毫秒");

        return o;
    }

}
