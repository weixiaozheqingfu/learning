package cn.huimin100.erp.aop.log.context;


import cn.huimin100.erp.aop.log.bean.ServiceOutputLogInfo;

public class ServiceOutputLogInfoContext {

    public static ThreadLocal<ServiceOutputLogInfo> threadLocal = new ThreadLocal<>();

    public static void remove() {
        threadLocal.remove();
    }

    public static ServiceOutputLogInfo get() {
        return threadLocal.get();
    }

    public static void set(ServiceOutputLogInfo serviceOutputLogInfo) {
        threadLocal.set(serviceOutputLogInfo);
    }

}
