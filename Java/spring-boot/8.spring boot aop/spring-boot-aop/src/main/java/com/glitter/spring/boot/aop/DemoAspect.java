package com.glitter.spring.boot.aop;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.MethodCallInfo;
import com.glitter.spring.boot.context.MethodCallInfoContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * // TODO 多个AOP的执行顺序?,是否会触发全局异常?
 *
 * 示例AOP切面
 * 建议使用before和AfterReturning,AfterThrowing,After配合使用,注意非常清晰的满足所有情况的流转.
 * 不建议使用around,执行时机和AfterReturning,AfterThrowing,After配合使用时,有时候会掰扯不请,不找这个麻烦和不痛快.
 *
 * 使用around通知的场景:在多线程环境下,在joinpoint调用目标方法后需要使用调用之前的方法局部变量是可以的。
 * 而如果使用Before和After也可以达到目的，但是就需要在aspect里面创建一个存储共享信息的field,而且这种做法并不是线程安全的。
 * 但这可以使用threadLocal解决。
 *
 * 原则:
 * 1.同时每一个通知方法都要进行try catch,也就是说通知方法不进行任何异常的抛出.
 * 2.around通知不和before通知同时使用.要么before和AfterReturning,AfterThrowing,After配合使用,要么around和AfterThrowing,After配合使用.
 */
@Aspect
@Component
public class DemoAspect {
    private static final Logger logger = LoggerFactory.getLogger(DemoAspect.class);

    /**
     * 定义拦截规则：拦截com.glitter.spring.boot.web.controller包下面的所有类中,有@RequestMapping注解的方法。
     */
    @Pointcut("execution(public * com.glitter.spring.boot.web.controller..(..)) and @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void demoAspectPointcut(){}

    /**
     * 目标方法执行之前调用
     * @param joinPoint
     * @throws Throwable
     */
    @Before("demoAspectPointcut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        try {


            System.out.print("before.....................................................................");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 目标方法执行完毕后正常返回数据时执行
     * @param joinPoint
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "demoAspectPointcut()")
    public void afterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
        try {
            logger.info("afterReturning...................................................................ret:{}",JSONObject.toJSONString(ret));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 目标方法抛出异常时执行
     * @param jp
     * @param ex
     */
    @AfterThrowing(throwing = "ex", pointcut = "demoAspectPointcut()")
    public void afterThrowing(JoinPoint jp, Exception ex){
        logger.info("afterReturning...................................................................ex:{}",JSONObject.toJSONString(ex));
    }

    /**
     * 目标方法调用后,不管目标方法是抛出异常或者正常执行完毕返回数据最后都会执行该通知方法,该通知为后置最终通知,final增强
     * @param jp
     */
    @After("demoAspectPointcut()")
    public void after(JoinPoint jp){
        logger.info("afterReturning...................................................................");
    }


    private void setMethodCallInfoContext(JoinPoint joinPoint){
        MethodCallInfo methodCallInfo = new MethodCallInfo();
        methodCallInfo.setClassName(this.getClassName(joinPoint));
        methodCallInfo.setMethodName(this.getMethodName(joinPoint));
        methodCallInfo.setParamNames(this.getMethodSignature(joinPoint).getParameterNames());
        methodCallInfo.setParamValues(joinPoint.getArgs());
        MethodCallInfoContext.set(methodCallInfo);
    }

    private Object[] getArgs(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        return args;
    }

    private MethodSignature getMethodSignature(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature;
    }

    private Method getMethod(JoinPoint joinPoint){
        Method method = this.getMethodSignature(joinPoint).getMethod();
        return method;
    }

    private String getMethodName(JoinPoint joinPoint){
        // String methodName1 = joinPoint.getSignature().getName(); 这种方式也可以
        String methodName = this.getMethod(joinPoint).getName();
        return methodName;
    }

    private String getClassName(JoinPoint joinPoint){
        String className = joinPoint.getTarget().getClass().getName();
        return className;
    }

    private Logger getLogger(JoinPoint joinPoint){
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        return logger;
    }

    private Map<String, Object> getParamMap(JoinPoint joinPoint) {
        // TODO 需要反复验证这两个的长度是否相等
        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = this.getMethodSignature(joinPoint).getParameterNames();


        for(int i=0;i<paramNames.length;i++) {
            System.out.println(paramNames[i] + "," + paramValues[i]);
        }

        Map<String,Object> argsMap = new HashMap<>();
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length != 0){
            for (int i=0;i<args.length;i++) {
                Object o = args[i];
                argsMap.put(String.valueOf(i), o);
            }
        }
        return argsMap;
    }

}