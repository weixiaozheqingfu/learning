package com.glitter.spring.boot.aop.log.context;


import com.glitter.spring.boot.aop.log.bean.RequestLogInfo;

public class RequestLogInfoContext {

    public static ThreadLocal<RequestLogInfo> threadLocal = new ThreadLocal<>();

    public static void remove() {
        threadLocal.remove();
    }

    public static RequestLogInfo get() {
        return threadLocal.get();
    }

    public static void set(RequestLogInfo requestLogInfo) {
        threadLocal.set(requestLogInfo);
    }

}
