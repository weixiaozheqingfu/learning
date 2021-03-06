package com.glitter.spring.boot.aop.log.aspect;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.aop.log.bean.ServiceInputLogInfo;
import com.glitter.spring.boot.aop.log.bean.ServiceOutputLogInfo;
import com.glitter.spring.boot.aop.log.context.ServiceInputLogInfoContext;
import com.glitter.spring.boot.aop.log.context.ServiceOutputLogInfoContext;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.util.TemplateUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author limengjun
 *
 *
 *
 * 不管目标方法返回值是void还是object
 * 正常情况: 1-->2-->3-->4
 *
 * 1.before异常:1-->3-->5
 * 2.target异常情况: 1-->2-->3-->5
 * 3.after异常情况: 1-->2-->3-->5
 * 4.afterReturning异常情况: 1-->2-->3-->4-->5
 * 5.afterThrowing异常情况: xx-->xx-->3--xx-->5
 *
 *
 * 正常一定会执行到4,
 * 异常一定会执行到5,
 * 不管是否有异常,一定会执行3。
 *
 *
 *
 * 当此处的5.afterThrowing执行了,异常继续往外抛,WebLogAspect等更外层的afterThrowing就不会执行了。异常会直接往外抛,可被全局异常捕获,如果没有配置全局异常捕获,则直接继续往最外层抛。
 *
 * try {
 *     try{
 *         // 1.@Before
 *         // 2.目标方法
 *         method.invoke(..);
 *     } finally {
 *         // 3.一定会执行
 *         //@After
 *     }
 *     // 4.如果前面抛异常,就不会执行
 *     //@AfterReturning
 * } catch(){
 *     // 5.抛异常时会执行
 *     //@AfterThrowing
 * }
 *
 */
@Aspect
@Component
@Order(2)
public class ServiceLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(public * com.glitter.spring.boot.service..*(..)) && @within(org.springframework.stereotype.Service)")
    public void serviceLogAspectPointcut(){}

    @Before("serviceLogAspectPointcut()")
    public void before(JoinPoint joinPoint) {
        try {
            ServiceInputLogInfoContext.remove();
            ServiceOutputLogInfoContext.remove();
            logger.info("service log before begin....................................................................");
            ServiceInputLogInfo serviceInputLogInfo = null == ServiceInputLogInfoContext.get() ? new ServiceInputLogInfo() : ServiceInputLogInfoContext.get();
            this.setServiceInputLogInfo(serviceInputLogInfo, joinPoint);
            logger.info("service log before,请求地址:{}", serviceInputLogInfo.getUri());
            if(null == ServiceInputLogInfoContext.get()){
                ServiceInputLogInfoContext.set(serviceInputLogInfo);
            }
            logger.info("service log before,param:{}", JSONObject.toJSONString(serviceInputLogInfo));
            logger.info("service log before end....................................................................");
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
        }
//        int i = 3/0;
    }

    /**
     * 同样,这里的代码块如果抛异常,也会“覆盖”目标方法的异常,是捕获这里的异常保证目标方法异常正常往外抛,还是将这里的异常往外抛“覆盖”目标方法的异常,取决于业务场景。
     * @param joinPoint
     */
    @After("serviceLogAspectPointcut()")
    public void after(JoinPoint joinPoint){
        try {
            logger.info("service log after begin....................................................................");
            ServiceOutputLogInfo serviceOutputLogInfo = null == ServiceOutputLogInfoContext.get() ? new ServiceOutputLogInfo() : ServiceOutputLogInfoContext.get();
            this.setServiceOutputLogInfo(serviceOutputLogInfo);
            if(null == ServiceOutputLogInfoContext.get()){
                ServiceOutputLogInfoContext.set(serviceOutputLogInfo);
            }
            logger.info("service log after end....................................................................");
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
        }
//        int i = 3/0;
    }

    @AfterReturning( pointcut = "serviceLogAspectPointcut()", returning = "ret")
    public void afterReturning(JoinPoint joinPoint, Object ret) {
        try {
            logger.info("service log afterReturning begin...........................................................");
            ServiceOutputLogInfo serviceOutputLogInfo = null == ServiceOutputLogInfoContext.get() ? new ServiceOutputLogInfo() : ServiceOutputLogInfoContext.get();
            Object result = ret instanceof Future ? ((Future) ret).get() : ret;
            serviceOutputLogInfo.setReturnObj(result);
            if(null == ServiceOutputLogInfoContext.get()){
                ServiceOutputLogInfoContext.set(serviceOutputLogInfo);
            }
            logger.info("service log afterReturning,result:{}", JSONObject.toJSONString(serviceOutputLogInfo));
            logger.info("service log afterReturning end...........................................................");
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
        } finally {
            ServiceInputLogInfoContext.remove();
            ServiceOutputLogInfoContext.remove();
        }
//        int i = 3/0;
    }

    /**
     * 这里只要自己的代码没有异常,或者有自己方法内部的异常捕获掉了,ex就会继续往外抛,
     * 如果自己的代码有异常没捕获往外抛了,那最后往外抛的就是自己的异常,而不是ex了。
     * 这里到底如何处理,看业务场景,比如这里如果是记录日志,这里的代码块执行的成败关系不大,不要掩盖掉真正的异常信息,那就再这里自己的代码有异常就捕获,记录异常信息即可,往外抛的还是ex。
     * 如果这里的代码也作为业务处理的一部分,那这里有异常也需要往外抛,那就记录ex,跑出去的就是这里的ex。
     *
     * 一般来讲,这里的代码尽量是稳定的,是没有异常的,然后保证抛出去的是真正的目标方法的异常为最好。
     *
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(pointcut = "serviceLogAspectPointcut()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex){
        try {
            logger.error("service log afterThrowing begin............................................................");
            ServiceOutputLogInfo serviceOutputLogInfo = null == ServiceOutputLogInfoContext.get() ? new ServiceOutputLogInfo() : ServiceOutputLogInfoContext.get();
            serviceOutputLogInfo.setEx(ex);
            if(null == ServiceOutputLogInfoContext.get()){
                ServiceOutputLogInfoContext.set(serviceOutputLogInfo);
            }
            if(ex instanceof BusinessException){
                logger.error("service log afterThrowing bussiness exception:{}", JSONObject.toJSONString(serviceOutputLogInfo));
            } else {
                logger.error("service log afterThrowing runtime exception:{}", JSONObject.toJSONString(serviceOutputLogInfo));
            }
            logger.error("service log afterThrowing end...........................................................");
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
        } finally {
            ServiceInputLogInfoContext.remove();
            ServiceOutputLogInfoContext.remove();
        }
//        int i = 3/0;
    }

    private void setServiceInputLogInfo(ServiceInputLogInfo serviceInputLogInfo, JoinPoint joinPoint){
        serviceInputLogInfo.setClassName(this.getClassName(joinPoint));
        serviceInputLogInfo.setMethodName(this.getMethodName(joinPoint));
        serviceInputLogInfo.setUri(serviceInputLogInfo.getClassName() + "." + serviceInputLogInfo.getMethodName());
        serviceInputLogInfo.setParamMap(this.getParamMap(joinPoint));
    }

    private void setServiceOutputLogInfo(ServiceOutputLogInfo serviceOutputLogInfo){
        ServiceInputLogInfo serviceInputLogInfo = ServiceInputLogInfoContext.get();
        serviceOutputLogInfo.setClassName(null == serviceInputLogInfo ? null : serviceInputLogInfo.getClassName());
        serviceOutputLogInfo.setMethodName(null == serviceInputLogInfo ? null : serviceInputLogInfo.getMethodName());
        serviceOutputLogInfo.setUri(null == serviceInputLogInfo ? null : serviceInputLogInfo.getUri());
        serviceOutputLogInfo.setParamMap(null == serviceInputLogInfo ? null : serviceInputLogInfo.getParamMap());
    }

    private MethodSignature getMethodSignature(JoinPoint joinPoint){
        return (MethodSignature) joinPoint.getSignature();
    }

    private Method getMethod(JoinPoint joinPoint){
        return this.getMethodSignature(joinPoint).getMethod();
    }

    private String getMethodName(JoinPoint joinPoint){
        String methodName = this.getMethod(joinPoint).getName();
        return methodName;
    }

    private String getClassName(JoinPoint joinPoint){
        String className = joinPoint.getTarget().getClass().getName();
        return className;
    }

    private Map<String, Object> getParamMap(JoinPoint joinPoint) {
        Map<String, Object> result = null;
        String[] paramNames = this.getMethodSignature(joinPoint).getParameterNames();
        Object[] paramValues = joinPoint.getArgs();
        if(null == paramNames && null == paramValues){
            return result;
        }
        if(0 == paramNames.length && 0 == paramValues.length){
            return result;
        }
        if(paramNames.length != paramValues.length){
            result = new LinkedHashMap<>();
            result.put("-1","输入参数异常");
            return result;
        }
        result = new LinkedHashMap<>();
        for (int i = 0; i < paramValues.length; i++) {
            if(paramValues[i] instanceof ServletRequest) { continue; }
            if(paramValues[i] instanceof HttpServletRequest) { continue; }
            if(paramValues[i] instanceof ServletResponse) { continue; }
            if(paramValues[i] instanceof HttpServletResponse) { continue; }
            result.put(paramNames[i], paramValues[i]);
        }
        return result;
    }

}