package com.glitter.spring.boot.context;

import com.glitter.spring.boot.bean.AccessTokenInParam;


public class AccessTokenInnerContext {

    public static ThreadLocal<AccessTokenInParam> threadLocal = new ThreadLocal<>();

    public static void set(AccessTokenInParam param) {
        threadLocal.set(param);
    }

    public static AccessTokenInParam get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

}
