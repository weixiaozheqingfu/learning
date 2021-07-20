package com.glitter.threadlocal;

import java.util.Date;

public class RequestTimeContext {

    private static final ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    public static Date get(){
        Date now = threadLocal.get();
        if (now == null) {
            now = new Date();
            threadLocal.set(now);
        }
        return now;
    }

    public static void set(Date date){
        threadLocal.set(date);
    }

    public static void remove(){
        threadLocal.remove();
    }

}
