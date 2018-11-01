package com.glitter.spring.boot.aop;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.context.RequestLogInfoContext;
import com.glitter.spring.boot.context.ResponseLogInfoContext;
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
 * @author Administrator
 */
@Aspect
@Component
@Order(2)
public class ServiceLogAspect {

//    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);
//
//    @Pointcut("execution(public * com.glitter.spring.boot.service..*(..)) and @annotation(org.springframework.stereotype.Service)")
//    public void serviceLogAspectPointcut(){}
//
//    @Before("serviceLogAspectPointcut()")
//    public void before(JoinPoint joinPoint) throws Throwable {
//        try {
//            logger.info("service log before begin....................................................................");
//            RequestLogInfo requestLogInfo = null == RequestLogInfoContext.get() ? new RequestLogInfo() : RequestLogInfoContext.get();
//            this.setRequestLogInfo(requestLogInfo, joinPoint);
//            logger.info("service log before,请求地址:{}", requestLogInfo.getUri());
//            if(null == ResponseLogInfoContext.get()){
//                RequestLogInfoContext.set(requestLogInfo);
//            }
//            logger.info("service log before end,输入参数:{}", JSONObject.toJSONString(requestLogInfo));
//        } catch (Exception e) {
//            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e, RequestLogInfoContext.get()));
//            throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
//        }
//    }
//
//    @After("serviceLogAspectPointcut()")
//    public void after(JoinPoint joinPoint){
//        try {
//            logger.info("service log after begin....................................................................");
//            ResponseLogInfo responseLogInfo = null == ResponseLogInfoContext.get() ? new ResponseLogInfo() : ResponseLogInfoContext.get();
//            this.setResponseLogInfo(responseLogInfo);
//            if(null == ResponseLogInfoContext.get()){
//                ResponseLogInfoContext.set(responseLogInfo);
//            }
//            logger.info("service log after end....................................................................");
//        } catch (Exception e) {
//            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e, RequestLogInfoContext.get()));
//            throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
//        }
//    }
//
//    @AfterReturning( pointcut = "serviceLogAspectPointcut()", returning = "ret")
//    public void afterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
//        try {
//            logger.info("service log afterReturning begin...........................................................");
//            ResponseLogInfo responseLogInfo = null == ResponseLogInfoContext.get() ? new ResponseLogInfo() : ResponseLogInfoContext.get();
//            responseLogInfo.setReturnObj(ret);
//            if(null == ResponseLogInfoContext.get()){
//                ResponseLogInfoContext.set(responseLogInfo);
//            }
//            logger.info("service log afterReturning end,输出参数:{}", JSONObject.toJSONString(responseLogInfo));
//        } catch (Exception e) {
//            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e, RequestLogInfoContext.get()));
//            throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
//        }
//    }
//
//    @AfterThrowing(pointcut = "serviceLogAspectPointcut()", throwing = "ex")
//    public void afterThrowing(JoinPoint joinPoint, Exception ex){
//        try {
//            logger.error("service log afterThrowing begin............................................................");
//            ResponseLogInfo responseLogInfo = null == ResponseLogInfoContext.get() ? new ResponseLogInfo() : ResponseLogInfoContext.get();
//            if(null == ResponseLogInfoContext.get()){
//                ResponseLogInfoContext.set(responseLogInfo);
//            }
//            if(ex instanceof BusinessException){
//                logger.error("service log afterThrowing end,目标方法业务异常:{}", JSONObject.toJSONString(responseLogInfo));
//            } else {
//                logger.error("service log afterThrowing end,目标方法运行异常:{}", JSONObject.toJSONString(responseLogInfo));
//            }
//        } catch (Exception e) {
//            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e, RequestLogInfoContext.get()));
//        }
//    }
//
//    private void setRequestLogInfo(RequestLogInfo requestLogInfo, JoinPoint joinPoint){
//        HttpServletRequest request = this.getRequest();
//        logger.debug("sessionId:"+request.getSession().getId());
//        requestLogInfo.setIp(null == request ? null : this.getIp(request));
//        requestLogInfo.setHost(null == request ? null : request.getRemoteHost());
//        requestLogInfo.setPort(null == request ? null : request.getRemotePort());
//        requestLogInfo.setUrl(null == request ? null : request.getRequestURL().toString());
//        requestLogInfo.setUri(null == request ? null : request.getRequestURI());
//        requestLogInfo.setClassName(this.getClassName(joinPoint));
//        requestLogInfo.setMethodName(this.getMethodName(joinPoint));
//        requestLogInfo.setRequestHeaderMap(this.getRequestHeaderMap());
//        requestLogInfo.setParamMap(this.getParamMap(joinPoint));
//    }
//
//    private void setResponseLogInfo(ResponseLogInfo responseLogInfo){
//        RequestLogInfo requestLogInfo = RequestLogInfoContext.get();
//        HttpServletResponse response = this.getResponse();
//        responseLogInfo.setIp(null == requestLogInfo ? null : requestLogInfo.getIp());
//        responseLogInfo.setHost(null == requestLogInfo ? null : requestLogInfo.getHost());
//        responseLogInfo.setPort(null == requestLogInfo ? null : requestLogInfo.getPort());
//        responseLogInfo.setUrl(null == requestLogInfo ? null : requestLogInfo.getUrl());
//        responseLogInfo.setUri(null == requestLogInfo ? null : requestLogInfo.getUri());
//        responseLogInfo.setClassName(null == requestLogInfo ? null : requestLogInfo.getClassName());
//        responseLogInfo.setMethodName(null == requestLogInfo ? null : requestLogInfo.getMethodName());
//        responseLogInfo.setParamMap(null == requestLogInfo ? null : requestLogInfo.getParamMap());
//        responseLogInfo.setRequestHeaderMap(null == requestLogInfo ? null : requestLogInfo.getRequestHeaderMap());
//        responseLogInfo.setResponseHeaderMap(this.getResponseHeaderMap());
//        // 如果业务方法或者本aop方法有异常,异常没有做捕获处理,而是继续往外抛,包括到全局异常都没有捕获处理,而是继续往抛,最终会抛到spring boot默认异常处理器,那么response的status值会被改写,通常是500。
//        responseLogInfo.setStatus(response.getStatus());
//        responseLogInfo.setContentType(response.getContentType());
//    }
//
//    private MethodSignature getMethodSignature(JoinPoint joinPoint){
//        return (MethodSignature) joinPoint.getSignature();
//    }
//
//    private Method getMethod(JoinPoint joinPoint){
//        return this.getMethodSignature(joinPoint).getMethod();
//    }
//
//    private String getMethodName(JoinPoint joinPoint){
//        // 这种方式也可以
//        // String methodName1 = joinPoint.getSignature().getName();
//        String methodName = this.getMethod(joinPoint).getName();
//        return methodName;
//    }
//
//    private String getClassName(JoinPoint joinPoint){
//        String className = joinPoint.getTarget().getClass().getName();
//        return className;
//    }
//
//    private Map<String, Object> getParamMap(JoinPoint joinPoint) {
//        Map<String, Object> result = null;
//        String[] paramNames = this.getMethodSignature(joinPoint).getParameterNames();
//        Object[] paramValues = joinPoint.getArgs();
//        if(null == paramNames && null == paramValues){
//            return result;
//        }
//        if(0 == paramNames.length && 0 == paramValues.length){
//            return result;
//        }
//        if(paramNames.length != paramValues.length){
//            result = new LinkedHashMap<>();
//            result.put("-1","输入参数异常");
//            return result;
//        }
//        result = new LinkedHashMap<>();
//        for (int i = 0; i < paramValues.length; i++) {
//            if(paramValues[i] instanceof ServletRequest) { continue; }
//            if(paramValues[i] instanceof HttpServletRequest) { continue; }
//            if(paramValues[i] instanceof ServletResponse) { continue; }
//            if(paramValues[i] instanceof HttpServletResponse) { continue; }
//            result.put(paramNames[i], paramValues[i]);
//        }
//        return result;
//    }

}