package com.glitter.spring.boot.context.log;


import com.glitter.spring.boot.bean.log.ResponseLogInfo;

public class ResponseLogInfoContext {

    public static ThreadLocal<ResponseLogInfo> threadLocal = new ThreadLocal<>();

    public static void remove() {
        threadLocal.remove();
    }

    public static ResponseLogInfo get() {
        return threadLocal.get();
    }

    public static void set(ResponseLogInfo responseLogInfo) {

        threadLocal.set(responseLogInfo);
    }

}
