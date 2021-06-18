package cn.huimin100.erp.aop.log.aspect;

import cn.huimin100.erp.aop.log.bean.ServiceInputLogInfo;
import cn.huimin100.erp.aop.log.bean.ServiceOutputLogInfo;
import com.alibaba.fastjson.JSONObject;
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
 */
@Aspect
@Component
@Order(2)
public class ServiceLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(* cn.huimin100.erp..*.domain..*(..))")
    public void pointcut1(){}

    @Pointcut("execution(* cn.huimin100.erp..*.service..*(..))")
    public void pointcut2(){}

    @Pointcut("execution(* cn.huimin100.erp..*.repository..*(..))")
    public void pointcut3(){}

    @Pointcut("@within(org.springframework.cloud.openfeign.FeignClient)")
    public void pointcut4(){}

    @Before("pointcut1() || pointcut2() || pointcut3() || pointcut4()")
    public void before(JoinPoint joinPoint) {
        try {
            ServiceInputLogInfo serviceInputLogInfo = new ServiceInputLogInfo();
            this.setServiceInputLogInfo(serviceInputLogInfo, joinPoint);
            logger.info("[" + serviceInputLogInfo.getUri() + "]-1-输入参数:{}", JSONObject.toJSONString(serviceInputLogInfo.getParamMap()));
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
        }
    }

    @AfterReturning( pointcut = "pointcut1() || pointcut2() || pointcut3() || pointcut4()", returning = "ret")
    public void afterReturning(JoinPoint joinPoint, Object ret) {
        try {
            ServiceOutputLogInfo serviceOutputLogInfo = new ServiceOutputLogInfo();
            this.setServiceOutputLogInfo(serviceOutputLogInfo, joinPoint, ret);
            logger.info("[" + serviceOutputLogInfo.getUri() + "]-3-输出参数:{}", JSONObject.toJSONString(serviceOutputLogInfo.getReturnObj()));
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
        }
    }

    @AfterThrowing(pointcut = "pointcut1() || pointcut2() || pointcut3() || pointcut4()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex){
        try {
            ServiceOutputLogInfo serviceOutputLogInfo = new ServiceOutputLogInfo();
            this.setServiceOutputLogInfo(serviceOutputLogInfo, joinPoint);
            serviceOutputLogInfo.setEx(ex);
            logger.info("[" + serviceOutputLogInfo.getUri() + "]-4-异常信息:{}", JSONObject.toJSONString(serviceOutputLogInfo.getEx()));
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
        }
    }

    @After("pointcut1() || pointcut2() || pointcut3() || pointcut4()")
    public void after(JoinPoint joinPoint){
        try {
            ServiceOutputLogInfo serviceOutputLogInfo = new ServiceOutputLogInfo();
            this.setServiceOutputLogInfo(serviceOutputLogInfo, joinPoint);
            logger.info("[" + serviceOutputLogInfo.getUri() + "]-5-执行完毕....................................................................");
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
        }
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
//        logger.info("joinPoint1:{}", JSONObject.toJSONString(joinPoint.getSignature().getDeclaringTypeName()));
//        logger.info("joinPoint2:{}", JSONObject.toJSONString(joinPoint.getTarget().getClass().getTypeName()));
//        logger.info("joinPoint3:{}", JSONObject.toJSONString(joinPoint.getTarget().getClass().getInterfaces()));
//        logger.info("joinPoint4:{}", JSONObject.toJSONString(joinPoint.getSignature().getName()));
//        logger.info("joinPoint5:{}", JSONObject.toJSONString(joinPoint.getTarget().getClass().getSimpleName()));
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