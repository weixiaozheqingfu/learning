package com.glitter.spring.boot.service.proxy;

import com.glitter.spring.boot.service.aop.JoinPoint;
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
        JoinPoint joinPoint = new JoinPoint(this.target, method, args);

        Object aspect = Class.forName(aspectInfo.getAspectName()).newInstance();
        Object result = null;
        try {
            if (method.getName().contains(aspectInfo.getBefore())) {
                Method beforeMethod = ClassUtilLimengjun.getMethod(aspect, "before");
                Object[] beforeArgs = new Object[]{joinPoint};
                beforeMethod.invoke(aspect, beforeArgs);
            }

            if (method.getName().contains(aspectInfo.getAround())) {
                Method aroundMethod = ClassUtilLimengjun.getMethod(aspect, "around");
                Object[] aroundArgs = new Object[]{joinPoint};
                result = aroundMethod.invoke(aspect, aroundArgs);
            } else {
                result = method.invoke(target, args);
            }

            if(method.getName().contains(aspectInfo.getAfter())){
                Method afterMethod = ClassUtilLimengjun.getMethod(aspect, "after");
                Object[] afterArgs = new Object[]{joinPoint};
                afterMethod.invoke(aspect, afterArgs);
            }

            if(method.getName().contains(aspectInfo.getAfterReturning())){
                Method afterReturningMethod = ClassUtilLimengjun.getMethod(aspect, "afterReturning");
                Object[] afterReturningArgs = new Object[]{joinPoint,result};
                afterReturningMethod.invoke(aspect, afterReturningArgs);
            }

            // 可能这个类中有的方法根本就没有匹配切入规则，那就直接调用方法本身。
            result = method.invoke(target, args);
        } catch (Exception e) {
            e.printStackTrace();
            if(method.getName().contains(aspectInfo.getAfterThrowing())){
                Method afterThrowingMethod = ClassUtilLimengjun.getMethod(aspect, "afterThrowing");
                Object[] afterThrowingArgs = new Object[]{joinPoint,e};
                afterThrowingMethod.invoke(aspect, afterThrowingArgs);
            }
        }
        return result;
    }

}
