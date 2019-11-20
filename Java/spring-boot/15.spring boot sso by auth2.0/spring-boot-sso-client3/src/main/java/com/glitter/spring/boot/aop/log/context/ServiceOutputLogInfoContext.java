package com.glitter.spring.boot.aop.log.context;


import com.glitter.spring.boot.aop.log.bean.ServiceOutputLogInfo;

public class ServiceOutputLogInfoContext {

    public static ThreadLocal<ServiceOutputLogInfo> threadLocal = new ThreadLocal<>();

    public static void remove() {
        threadLocal.remove();
    }

    public static ServiceOutputLogInfo get() {
        return threadLocal.get();
    }

    public static void set(ServiceOutputLogInfo serviceOutputLogInfo) {
        threadLocal.set(serviceOutputLogInfo);
    }

}
