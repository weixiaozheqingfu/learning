package com.glitter.spring.boot.service.proxy;

import com.glitter.spring.boot.util.AspectInfo;
import com.glitter.spring.boot.util.ClassUtilLimengjun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class ProxyInvocationHandler implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(ProxyInvocationHandler.class);

    private Object target;

    private AspectInfo aspectInfo;

    public ProxyInvocationHandler(Object target, AspectInfo aspectInfo) {
        this.target = target;
        this.aspectInfo = aspectInfo;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // TODO 将null替换为JoinPoint对象

        List<String> targetMethodNames = ClassUtilLimengjun.getPublicMethodNames(target);
        Object aspect = Class.forName(aspectInfo.getAspectName()).newInstance();
        Object result = null;
        try {
            if (targetMethodNames.contains(aspectInfo.getBefore())) {
                Method beforeMethod = ClassUtilLimengjun.getMethod(aspect, "before");
                beforeMethod.invoke(aspect, null);
            }

            if (targetMethodNames.contains(aspectInfo.getAround())) {
                Method aroundMethod = ClassUtilLimengjun.getMethod(aspect, "around");
                result = aroundMethod.invoke(aspect, null);
            } else {
                result = method.invoke(target, args);
            }

            if(targetMethodNames.contains(aspectInfo.getAfter())){
                Method afterMethod = ClassUtilLimengjun.getMethod(aspect, "after");
                afterMethod.invoke(aspect, null);
            }

            if(targetMethodNames.contains(aspectInfo.getAfterReturning())){
                Method afterReturningMethod = ClassUtilLimengjun.getMethod(aspect, "afterReturning");
                afterReturningMethod.invoke(aspect, null, null);
            }
        } catch (Exception e) {
            if(targetMethodNames.contains(aspectInfo.getAfterThrowing())){
                Method afterThrowingMethod = ClassUtilLimengjun.getMethod(aspect, "afterThrowing");
                afterThrowingMethod.invoke(aspect, null, e);
            }
        }
        return result;
    }

}
