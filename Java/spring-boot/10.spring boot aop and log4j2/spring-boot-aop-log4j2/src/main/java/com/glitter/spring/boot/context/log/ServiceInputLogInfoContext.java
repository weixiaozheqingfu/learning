package com.glitter.spring.boot.context.log;


import com.glitter.spring.boot.bean.log.ServiceInputLogInfo;

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
