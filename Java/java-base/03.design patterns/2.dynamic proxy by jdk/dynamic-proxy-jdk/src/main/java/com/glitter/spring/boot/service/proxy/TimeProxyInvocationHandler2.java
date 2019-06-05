package com.glitter.spring.boot.service.proxy;

import com.glitter.spring.boot.service.impl.WeChatPayServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TimeProxyInvocationHandler2 implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(TimeProxyInvocationHandler2.class);

    private Object target;

    public TimeProxyInvocationHandler2(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (target instanceof WeChatPayServiceImpl) {
            if (((WeChatPayServiceImpl) target).isFlag()) {
                logger.info("TimeProxyInvocationHandler.flag:" + ((WeChatPayServiceImpl) target).isFlag());
            }else {
                logger.info("TimeProxyInvocationHandler.flag:" + ((WeChatPayServiceImpl) target).isFlag());
            }
        }

        Long beginTime = System.currentTimeMillis();
        logger.info("TimeProxyInvocationHandler开始执行,开始执行时间:"+beginTime);

        Object o = method.invoke(target, args);

        Long endTime = System.currentTimeMillis();
        logger.info("TimeProxyInvocationHandler执行完毕,执行完毕时间:"+endTime);
        logger.info("方法执行耗时:" + (endTime - beginTime) + "毫秒");

        return o;
    }

}
