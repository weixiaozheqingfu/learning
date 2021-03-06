package com.glitter.spring.boot.aop.log.aspect;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.aop.log.bean.ServiceInputLogInfo;
import com.glitter.spring.boot.aop.log.bean.ServiceOutputLogInfo;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author limengjun
 *
 * 以下顺序是基于spring aop的jar包版本是5.0.4.RELEASE的结论。对应的<artifactId>spring-boot-starter-parent</artifactId>的版本是<version>2.3.6.RELEASE</version>
 * 如果spring aop的jar包版本是5.2.11.RELEASE,那么after和afterReturning的顺序是反着的,对应的<artifactId>spring-boot-starter-parent</artifactId>的版本是<version>2.0.0.RELEASE</version>
 * 但是不管如何,after始终都会执行。
 * 这种不一样完全在于spring aop升级版本后,里面源码的写法发生了变化,导致顺序执行有了变化,不过影响不大。其实两种写法的顺序都是可以做到的,可能后续作者觉得这样的顺序更合理,实不相瞒,我也觉得先执行afterReturning再执行after更舒服一点。
 *
 * // 1.@Before
 * try {
 *      // 2.目标方法
 *      Object o = method.invoke(..);
 *      // 4.如果前面抛异常,就不会执行
 *      //@AfterReturning
 * } catch(){
 *     // 5.抛异常时会执行
 *     //@AfterThrowing
 * } finally {
 *     // 3.一定会执行
 *     //@After
 * }
 *
 * 下面的是5.2.11.RELEASE版本的顺序
 * 正常情况: 1-->2-->4-->3
 *
 * 1.before异常:1-->往外抛异常-->如果外层还有aop拦截-->会走外层的afterThrowing-->如果外层没有-->直接往外抛--->这种逻辑的调整也比较好,把before等这些方法当成常规常见方法调用栈处理方式处理了,本来有异常了就直接往上层抛就好了。相当于源码不再做捕获后执行指定方法后再抛了,所有就是常规直接向上抛的效果了。简单直接挺好的。
 * 2.target异常情况: 1-->2-->5-->3-->异常往外继续抛-->
 * 3.after异常情况: 1-->2-->4-->3-->异常往外继续抛-->
 * 4.afterReturning异常情况: 1-->2-->4-->3-->异常往外继续抛-->
 * 5.afterThrowing异常情况: xx-->xx-->xx-->5-->3-->异常往外继续抛-->
 * 果然这个顺序更舒服了。
 *
 * 以后再有其他spring aop版本升级如果发现顺序不一致,随时可以将升级后的版本在这个项目中引入过来来尝试,各种情况顺序很容易就试出来了,然后可以再看看源码处理的变化.
 *
 *
 * 下面的是5.0.4.RELEASE版本的顺序:
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
 * 不管目标方法返回值是void还是object
 * 正常情况: 1-->2-->3-->4
 *
 * 1.before异常:1-->3-->5-->异常往外继续抛-->
 * 2.target异常情况: 1-->2-->3-->5-->异常往外继续抛-->
 * 3.after异常情况: 1-->2-->3-->5-->异常往外继续抛-->
 * 4.afterReturning异常情况: 1-->2-->3-->4-->5-->异常往外继续抛-->
 * 5.afterThrowing异常情况: xx-->xx-->3--xx-->5-->异常往外继续抛-->
 *
 * 正常一定会执行到4,
 * 异常一定会执行到5,
 * 不管是否有异常,一定会执行3。
 *
 * 当此处的5.afterThrowing执行了,异常继续往外抛,WebLogAspect等更外层的afterThrowing就不会执行了。异常会直接往外抛,可被全局异常捕获,如果没有配置全局异常捕获,则直接继续往最外层抛。
 *
 *
 *
 *
 *
 * 我把spring aop的jar包版本是5.0.4.RELEASE的结论。对应的<artifactId>spring-boot-starter-parent</artifactId>的版本是<version>2.3.6.RELEASE</version>再重新整理一版舒服的顺序,如下:
 * // 1.@Before
 * try {
 *      // 2.目标方法
 *      Object o = method.invoke(..);
 *      // 3.如果前面抛异常,就不会执行
 *      //@AfterReturning
 * } catch(){
 *     // 4.抛异常时会执行
 *     //@AfterThrowing
 * } finally {
 *     // 5.一定会执行
 *     //@After
 * }
 *
 * 正常情况: 1-->2-->3-->5
 *
 * 1.before异常:1-->往外抛异常-->如果外层还有aop拦截-->会走外层的afterThrowing-->如果外层没有-->直接往外抛--->这种逻辑的调整也比较好,把before等这些方法当成常规常见方法调用栈处理方式处理了,本来有异常了就直接往上层抛就好了。相当于源码不再做捕获后执行指定方法后再抛了,所有就是常规直接向上抛的效果了。简单直接挺好的。
 * 2.target异常情况: 1-->2-->4-->5-->异常往外继续抛-->
 * 3.after异常情况: 1-->2-->3-->5-->异常往外继续抛-->
 * 4.afterReturning异常情况: 1-->2-->3-->4-->异常往外继续抛-->
 * 5.afterThrowing异常情况: xx-->xx-->xx-->4-->5-->异常往外继续抛-->
 *
 */
@Aspect
@Component
@Order(2)
public class ServiceLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

//    @Pointcut("execution(public * com.glitter.spring.boot.service..*(..)) && @within(org.springframework.stereotype.Service)")
//    public void serviceLogAspectPointcut(){}

    @Pointcut("execution(* com.glitter..*.domain..*(..))")
    public void pointcut1(){}

    @Pointcut("execution(* com.glitter..*.service..*(..))")
    public void pointcut2(){}

    @Pointcut("execution(* com.glitter..*.repository..*(..))")
    public void pointcut3() {}

//    @Pointcut("@within(org.springframework.cloud.openfeign.FeignClient)")
//    public void pointcut4() {}

    @Before("pointcut1() || pointcut2() || pointcut3()")
    public void before(JoinPoint joinPoint) {
        try {
            ServiceInputLogInfo serviceInputLogInfo = new ServiceInputLogInfo();
            this.setServiceInputLogInfo(serviceInputLogInfo, joinPoint);
            logger.info("[" + serviceInputLogInfo.getUri() + "]我是------1-------");
            logger.info("[" + serviceInputLogInfo.getUri() + "]-1-输入参数:{}", JSONObject.toJSONString(serviceInputLogInfo.getParamMap()));
//          logger.info("[" + serviceInputLogInfo.getUri() + "]输入参数:{}", JSONObject.toJSONString(serviceInputLogInfo.getParamMap(), SerializerFeature.WriteMapNullValue));
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
        }
//      int i = 3/0;
    }

    @AfterReturning( pointcut = "pointcut1() || pointcut2() || pointcut3()", returning = "ret")
    public void afterReturning(JoinPoint joinPoint, Object ret) {
        try {
            ServiceOutputLogInfo serviceOutputLogInfo = new ServiceOutputLogInfo();
            this.setServiceOutputLogInfo(serviceOutputLogInfo, joinPoint, ret);
            logger.info("[" + serviceOutputLogInfo.getUri() + "]我是------3-------");
            logger.info("[" + serviceOutputLogInfo.getUri() + "]-3-输出参数:{}", JSONObject.toJSONString(serviceOutputLogInfo.getReturnObj()));
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
        }
//      int i = 3/0;
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
    @AfterThrowing(pointcut = "pointcut1() || pointcut2() || pointcut3()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex){
        try {
            ServiceOutputLogInfo serviceOutputLogInfo = new ServiceOutputLogInfo();
            this.setServiceOutputLogInfo(serviceOutputLogInfo, joinPoint);
            serviceOutputLogInfo.setEx(ex);
            logger.info("[" + serviceOutputLogInfo.getUri() + "]我是------4-------");
            logger.info("[" + serviceOutputLogInfo.getUri() + "]-4-异常信息:{}", JSONObject.toJSONString(serviceOutputLogInfo.getEx()));
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
        }
        int i = 3/0;
    }

    /**
     * 同样,这里的代码块如果抛异常,也会“覆盖”目标方法的异常,是捕获这里的异常保证目标方法异常正常往外抛,还是将这里的异常往外抛“覆盖”目标方法的异常,取决于业务场景。
     * @param joinPoint
     */
    @After("pointcut1() || pointcut2() || pointcut3()")
    public void after(JoinPoint joinPoint){
        try {
            ServiceOutputLogInfo serviceOutputLogInfo = new ServiceOutputLogInfo();
            this.setServiceOutputLogInfo(serviceOutputLogInfo, joinPoint);
            logger.info("[" + serviceOutputLogInfo.getUri() + "]我是------5-------");
            logger.info("[" + serviceOutputLogInfo.getUri() + "]-5-执行完毕....................................................................");
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
        }
//      int i = 3/0;
    }

    private void setServiceInputLogInfo(ServiceInputLogInfo serviceInputLogInfo, JoinPoint joinPoint){
        serviceInputLogInfo.setClassName(this.getClassName(joinPoint));
        serviceInputLogInfo.setMethodName(this.getMethodName(joinPoint));
        serviceInputLogInfo.setUri(serviceInputLogInfo.getClassName() + "." + serviceInputLogInfo.getMethodName());
        serviceInputLogInfo.setParamMap(this.getParamMap(joinPoint));
    }

    private void setServiceOutputLogInfo(ServiceOutputLogInfo serviceOutputLogInfo, JoinPoint joinPoint) throws ExecutionException, InterruptedException {
        this.setServiceOutputLogInfo(serviceOutputLogInfo, joinPoint, null);
    }

    private void setServiceOutputLogInfo(ServiceOutputLogInfo serviceOutputLogInfo, JoinPoint joinPoint, Object ret) throws ExecutionException, InterruptedException {
        serviceOutputLogInfo.setClassName(this.getClassName(joinPoint));
        serviceOutputLogInfo.setMethodName(this.getMethodName(joinPoint));
        serviceOutputLogInfo.setUri(serviceOutputLogInfo.getClassName() + "." + serviceOutputLogInfo.getMethodName());
        serviceOutputLogInfo.setParamMap(this.getParamMap(joinPoint));
        if (ret != null) {
            Object returnObj = ret instanceof Future ? ((Future) ret).get() : ret;
            serviceOutputLogInfo.setReturnObj(returnObj);
        }
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
//      String className = joinPoint.getTarget().getClass().getName();
//      String className = joinPoint.getTarget().getClass().getSimpleName();
//      logger.info("joinPoint1:{}", JSONObject.toJSONString(joinPoint.getSignature().getDeclaringTypeName()));
//      logger.info("joinPoint2:{}", JSONObject.toJSONString(joinPoint.getTarget().getClass().getTypeName()));
//      logger.info("joinPoint3:{}", JSONObject.toJSONString(joinPoint.getTarget().getClass().getInterfaces()));
//      logger.info("joinPoint4:{}", JSONObject.toJSONString(joinPoint.getSignature().getName()));
//      logger.info("joinPoint5:{}", JSONObject.toJSONString(joinPoint.getTarget().getClass().getSimpleName()));
        String className = joinPoint.getSignature().getDeclaringTypeName().substring(joinPoint.getSignature().getDeclaringTypeName().lastIndexOf(".") + 1);
        return className;
    }

    private Map<String, Object> getParamMap(JoinPoint joinPoint) {
        Map<String, Object> result = null;
        String[] paramNames = this.getMethodSignature(joinPoint).getParameterNames();
        Object[] paramValues = joinPoint.getArgs();
        if(null == paramNames && null == paramValues){
            return result;
        }
        if (null == paramNames && null != paramValues) {
            result = new LinkedHashMap<>();
            result.put("paramNames", paramNames);
            result.put("paramValues", paramValues);
            result.put("-1", "ServiceLogAspect拦截输入参数异常,paramNames与paramValues个数不匹配");
            return result;
        }
        if (null != paramNames && null == paramValues) {
            result = new LinkedHashMap<>();
            result.put("paramNames", paramNames);
            result.put("paramValues", paramValues);
            result.put("-2", "ServiceLogAspect拦截输入参数异常,paramNames与paramValues个数不匹配");
            return result;
        }
        if(0 == paramNames.length && 0 == paramValues.length){
            return result;
        }
        if(paramNames.length != paramValues.length){
            result = new LinkedHashMap<>();
            result.put("paramNames", paramNames);
            result.put("paramValues", paramValues);
            result.put("-3", "ServiceLogAspect拦截输入参数异常,paramNames与paramValues个数不匹配");
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