package com.glitter.spring.boot.context;

public class JsessionIdResponseContext {

    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void set(String param) {
        threadLocal.set(param);
    }

    public static String get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

}
