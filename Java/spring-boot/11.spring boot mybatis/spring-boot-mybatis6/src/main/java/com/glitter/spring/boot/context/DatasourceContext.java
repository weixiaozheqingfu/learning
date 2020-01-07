package com.glitter.spring.boot.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description 这里切换读/写模式
 * 原理是利用ThreadLocal保存当前线程是否处于读模式（通过开始READ_ONLY注解在开始操作前设置模式为读模式，
 * 操作结束后清除该数据，避免内存泄漏，同时也为了后续在该线程进行写操作时任然为读模式
 */
public class DatasourceContext {

    private static Logger log = LoggerFactory.getLogger(DatasourceContext.class);

    public static final String WRITE = "write";
    public static final String READ = "read";

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void set(String dsType) {
        if (dsType == null) {
            log.error("dsType为空");
            throw new NullPointerException();
        }
        log.info("设置dsType为：{}",dsType);
        threadLocal.set(dsType);
    }

    public static String get() {
        return (threadLocal.get() == null) ? WRITE : threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

}
