package com.glitter.spring.boot.context;

import com.glitter.spring.boot.bean.mq.PublishCallBackInfo;

public class RabbitmqContext {

    public static ThreadLocal<PublishCallBackInfo> threadLocal = new ThreadLocal<>();

    public static void set(PublishCallBackInfo param) {
        threadLocal.set(param);
    }

    public static PublishCallBackInfo get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

}
