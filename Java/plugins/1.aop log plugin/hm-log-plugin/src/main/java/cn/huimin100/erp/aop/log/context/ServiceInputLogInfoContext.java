package cn.huimin100.erp.aop.log.context;


import cn.huimin100.erp.aop.log.bean.ServiceInputLogInfo;

public class ServiceInputLogInfoContext {

    public static ThreadLocal<ServiceInputLogInfo> threadLocal = new ThreadLocal<>();

    public static void remove() {
        threadLocal.remove();
    }

    public static ServiceInputLogInfo get() {
        return threadLocal.get();
    }

    public static void set(ServiceInputLogInfo serviceInputLogInfo) {
        threadLocal.set(serviceInputLogInfo);
    }

}
