package com.glitter.spring.boot.context.log;


import com.glitter.spring.boot.bean.log.ServiceOutputLogInfo;

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
