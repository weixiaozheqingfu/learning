package cn.huimin100.erp.aop.log.aspect;

import cn.huimin100.erp.aop.log.context.ServiceInputLogInfoContext;
import cn.huimin100.erp.aop.log.context.ServiceOutputLogInfoContext;
import com.alibaba.fastjson.JSONObject;
import cn.huimin100.erp.aop.log.bean.ServiceInputLogInfo;
import cn.huimin100.erp.aop.log.bean.ServiceOutputLogInfo;
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

    @Before("pointcut1() || pointcut2()")
    public void before(JoinPoint joinPoint) {
        try{
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
//          throw e;
        }
    }

    @After("pointcut1() || pointcut2()")
    public void after(JoinPoint joinPoint){
        try{
            logger.info("service log after begin....................................................................");
            ServiceOutputLogInfo serviceOutputLogInfo = null == ServiceOutputLogInfoContext.get() ? new ServiceOutputLogInfo() : ServiceOutputLogInfoContext.get();
            this.setServiceOutputLogInfo(serviceOutputLogInfo);
            if(null == ServiceOutputLogInfoContext.get()){
                ServiceOutputLogInfoContext.set(serviceOutputLogInfo);
            }
            logger.info("service log after end....................................................................");
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
//          throw e;
        }
    }

    @AfterReturning( pointcut = "pointcut1() || pointcut2()", returning = "ret")
    public void afterReturning(JoinPoint joinPoint, Object ret) {
        try{
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
//          throw e;
        } finally{
            ServiceInputLogInfoContext.remove();
            ServiceOutputLogInfoContext.remove();
        }
    }

    @AfterThrowing(pointcut = "pointcut1() || pointcut2()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
        try {
            logger.error("service log afterThrowing begin............................................................");
            ServiceOutputLogInfo serviceOutputLogInfo = null == ServiceOutputLogInfoContext.get() ? new ServiceOutputLogInfo() : ServiceOutputLogInfoContext.get();
            serviceOutputLogInfo.setEx(ex);
            if(null == ServiceOutputLogInfoContext.get()){
                ServiceOutputLogInfoContext.set(serviceOutputLogInfo);
            }
            logger.error("service log afterThrowing exception:{}", JSONObject.toJSONString(serviceOutputLogInfo));
            logger.error("service log afterThrowing end............................................................");
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
//          throw e;
        } finally {
            ServiceInputLogInfoContext.remove();
            ServiceOutputLogInfoContext.remove();
        }
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