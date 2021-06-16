package cn.huimin100.erp.aop.log.context;


import cn.huimin100.erp.aop.log.bean.RequestLogInfo;

public class RequestLogInfoContext {

    public static ThreadLocal<RequestLogInfo> threadLocal = new ThreadLocal<>();

    public static void remove() {
        threadLocal.remove();
    }

    public static RequestLogInfo get() {
        return threadLocal.get();
    }

    public static void set(RequestLogInfo requestLogInfo) {
        threadLocal.set(requestLogInfo);
    }

}
