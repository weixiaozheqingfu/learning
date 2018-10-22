package com.glitter.spring.boot.context;

import com.glitter.spring.boot.bean.MethodCallInfo;

public class MethodCallInfoContext {

    public static ThreadLocal<MethodCallInfo> threadLocal = new ThreadLocal<>();

    public static void remove() {
        threadLocal.remove();
    }

    public static MethodCallInfo get() {
        return threadLocal.get();
    }

    public static void set(MethodCallInfo methodCallInfo) {

        threadLocal.set(methodCallInfo);
    }

}
