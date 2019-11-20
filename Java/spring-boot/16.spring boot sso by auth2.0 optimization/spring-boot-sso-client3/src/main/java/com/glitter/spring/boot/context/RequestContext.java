package com.glitter.spring.boot.context;

import javax.servlet.http.HttpServletRequest;


public class RequestContext {

    public static ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<>();

    public static void set(HttpServletRequest param) {
        threadLocal.set(param);
    }

    public static HttpServletRequest get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

}
