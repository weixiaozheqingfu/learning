package cn.huimin100.erp.aop.log.context;


import cn.huimin100.erp.aop.log.bean.ResponseLogInfo;

public class ResponseLogInfoContext {

    public static ThreadLocal<ResponseLogInfo> threadLocal = new ThreadLocal<>();

    public static void remove() {
        threadLocal.remove();
    }

    public static ResponseLogInfo get() {
        return threadLocal.get();
    }

    public static void set(ResponseLogInfo responseLogInfo) {

        threadLocal.set(responseLogInfo);
    }

}
