package com.glitter.spring.boot.context;

import javax.servlet.http.HttpServletResponse;

public class ResponseContext {

    public static ThreadLocal<HttpServletResponse> threadLocal = new ThreadLocal<>();

    public static void set(HttpServletResponse param) {
        threadLocal.set(param);
    }

    public static HttpServletResponse get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

}
