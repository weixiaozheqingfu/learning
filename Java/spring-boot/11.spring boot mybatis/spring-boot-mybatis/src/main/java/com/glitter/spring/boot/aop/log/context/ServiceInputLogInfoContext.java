package com.glitter.spring.boot.aop.log.context;


import com.glitter.spring.boot.aop.log.bean.ServiceInputLogInfo;

public class ServiceInputLogInfoContext {

    public static ThreadLocal<ServiceInputLogInfo> threadLocal = new ThreadLocal<>();

    public static void remove() {
        threadLocal.remove();
    }

    public static ServiceInputLogInfo get() {
        return threadLocal.get();
    }

    public static void set(ServiceInputLogInfo serviceInputLogInfo) {
        threadLocal.set(serviceInputLogInfo);
    }

}
