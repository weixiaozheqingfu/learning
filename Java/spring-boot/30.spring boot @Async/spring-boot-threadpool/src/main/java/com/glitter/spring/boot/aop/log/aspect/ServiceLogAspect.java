package com.glitter.spring.boot.aop.log.aspect;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.aop.log.bean.ServiceInputLogInfo;
import com.glitter.spring.boot.aop.log.bean.ServiceOutputLogInfo;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.aop.log.context.RequestLogInfoContext;
import com.glitter.spring.boot.aop.log.context.ServiceInputLogInfoContext;
import com.glitter.spring.boot.aop.log.context.ServiceOutputLogInfoContext;
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

/**
 * @author limengjun
 */
@Aspect
@Component
@Order(2)
public class ServiceLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

//    @Pointcut("execution(public * com.glitter.spring.boot.service..*(..)) and @annotation(org.springframework.stereotype.Service)")
    public void serviceLogAspectPointcut(){}

//    @Before("serviceLogAspectPointcut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        try {
            logger.info("service log before begin....................................................................");
            ServiceInputLogInfo serviceInputLogInfo = null == ServiceInputLogInfoContext.get() ? new ServiceInputLogInfo() : ServiceInputLogInfoContext.get();
            this.setServiceInputLogInfo(serviceInputLogInfo, joinPoint);
            logger.info("service log before,请求地址:{}", serviceInputLogInfo.getUri());
            if(null == ServiceInputLogInfoContext.get()){
                ServiceInputLogInfoContext.set(serviceInputLogInfo);
            }
            logger.info("service log before,param:{}", JSONObject.toJSONString(serviceInputLogInfo));
            logger.info("service log before end....................................................................");
        } catch (Exception e) {
            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e, ServiceInputLogInfoContext.get()));
            throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
        }
    }

//    @After("serviceLogAspectPointcut()")
    public void after(JoinPoint joinPoint){
        try {
            logger.info("service log after begin....................................................................");
            ServiceOutputLogInfo serviceOutputLogInfo = null == ServiceOutputLogInfoContext.get() ? new ServiceOutputLogInfo() : ServiceOutputLogInfoContext.get();
            this.setServiceOutputLogInfo(serviceOutputLogInfo);
            if(null == ServiceOutputLogInfoContext.get()){
                ServiceOutputLogInfoContext.set(serviceOutputLogInfo);
            }
            logger.info("service log after end....................................................................");
        } catch (Exception e) {
            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e, RequestLogInfoContext.get()));
            throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
        }
    }

//    @AfterReturning( pointcut = "serviceLogAspectPointcut()", returning = "ret")
    public void afterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
        try {
            logger.info("service log afterReturning begin...........................................................");
            ServiceOutputLogInfo serviceOutputLogInfo = null == ServiceOutputLogInfoContext.get() ? new ServiceOutputLogInfo() : ServiceOutputLogInfoContext.get();
            serviceOutputLogInfo.setReturnObj(ret);
            if(null == ServiceOutputLogInfoContext.get()){
                ServiceOutputLogInfoContext.set(serviceOutputLogInfo);
            }
            logger.info("service log afterReturning,result:{}", JSONObject.toJSONString(serviceOutputLogInfo));
            logger.info("service log afterReturning end...........................................................");
        } catch (Exception e) {
            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e, RequestLogInfoContext.get()));
            throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
        }
    }

//    @AfterThrowing(pointcut = "serviceLogAspectPointcut()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex){
        try {
            logger.error("service log afterThrowing begin............................................................");
            ServiceOutputLogInfo serviceOutputLogInfo = null == ServiceOutputLogInfoContext.get() ? new ServiceOutputLogInfo() : ServiceOutputLogInfoContext.get();
            serviceOutputLogInfo.setEx(ex);
            if(null == ServiceOutputLogInfoContext.get()){
                ServiceOutputLogInfoContext.set(serviceOutputLogInfo);
            }
            if(ex instanceof BusinessException){
                logger.error("service log afterThrowing end,target method bussiness exception:{}", JSONObject.toJSONString(serviceOutputLogInfo));
            } else {
                logger.error("service log afterThrowing end,target method runtime exception:{}", JSONObject.toJSONString(serviceOutputLogInfo));
            }
        } catch (Exception e) {
            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e, RequestLogInfoContext.get()));
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