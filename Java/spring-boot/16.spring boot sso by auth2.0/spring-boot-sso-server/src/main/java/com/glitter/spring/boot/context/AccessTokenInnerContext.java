package com.glitter.spring.boot.context;

import com.glitter.spring.boot.bean.AccessTokenInner;


public class AccessTokenInnerContext {

    public static ThreadLocal<AccessTokenInner> threadLocal = new ThreadLocal<>();

    public static void set(AccessTokenInner param) {
        threadLocal.set(param);
    }

    public static AccessTokenInner get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

}
