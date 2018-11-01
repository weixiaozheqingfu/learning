package com.glitter.spring.boot.context;


import com.glitter.spring.boot.bean.log.RequestLogInfo;

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
