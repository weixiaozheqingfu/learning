package com.glitter.spring.boot.aop.log.aspect;

import com.alibaba.fastjson.JSONObject;

import com.glitter.spring.boot.aop.log.bean.RequestLogInfo;
import com.glitter.spring.boot.aop.log.bean.ResponseLogInfo;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.aop.log.context.RequestLogInfoContext;
import com.glitter.spring.boot.aop.log.context.ResponseLogInfoContext;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.util.TemplateUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.servlet.support.WebContentGenerator;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author limengjun
 */
@Aspect
@Component
@Order(1)
public class WebLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(public * com.glitter.spring.boot.web.action..*(..)) and @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void webLogAspectPointcut(){}

    @Before("webLogAspectPointcut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        try {
            logger.info("web log before begin....................................................................");
            RequestLogInfo requestLogInfo = null == RequestLogInfoContext.get() ? new RequestLogInfo() : RequestLogInfoContext.get();
            this.setRequestLogInfo(requestLogInfo, joinPoint);
            logger.info("web log before,addr:{}", requestLogInfo.getUri());
            if(null == ResponseLogInfoContext.get()){
                RequestLogInfoContext.set(requestLogInfo);
            }
            logger.info("web log before,param:{}", JSONObject.toJSONString(requestLogInfo));
            logger.info("web log before end....................................................................");
        } catch (Exception e) {
            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e, RequestLogInfoContext.get()));
            throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
        }
    }

    @After("webLogAspectPointcut()")
    public void after(JoinPoint joinPoint){
        try {
            logger.info("web log after begin....................................................................");
            ResponseLogInfo responseLogInfo = null == ResponseLogInfoContext.get() ? new ResponseLogInfo() : ResponseLogInfoContext.get();
            this.setResponseLogInfo(responseLogInfo);
            if(null == ResponseLogInfoContext.get()){
                ResponseLogInfoContext.set(responseLogInfo);
            }
            logger.info("web log after end....................................................................");
        } catch (Exception e) {
            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e, RequestLogInfoContext.get()));
            throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
        }
    }

    @AfterReturning( pointcut = "webLogAspectPointcut()", returning = "ret")
    public void afterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
        try {
            logger.info("web log afterReturning begin...........................................................");
            ResponseLogInfo responseLogInfo = null == ResponseLogInfoContext.get() ? new ResponseLogInfo() : ResponseLogInfoContext.get();
            responseLogInfo.setReturnObj(ret);
            if(null == ResponseLogInfoContext.get()){
                ResponseLogInfoContext.set(responseLogInfo);
            }
            logger.info("web log afterReturning,result:{}", JSONObject.toJSONString(responseLogInfo));
            logger.info("web log afterReturning end...........................................................");
        } catch (Exception e) {
            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e, RequestLogInfoContext.get()));
            throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
        }
    }

    @AfterThrowing(pointcut = "webLogAspectPointcut()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex){
        try {
            logger.error("web log afterThrowing begin............................................................");
            ResponseLogInfo responseLogInfo = null == ResponseLogInfoContext.get() ? new ResponseLogInfo() : ResponseLogInfoContext.get();
            responseLogInfo.setEx(ex);
            responseLogInfo.setStatus(500);
            if(null == ResponseLogInfoContext.get()){
                ResponseLogInfoContext.set(responseLogInfo);
            }
            if(ex instanceof BusinessException){
                logger.error("web log afterThrowing end,target method bussiness exception:{}", JSONObject.toJSONString(responseLogInfo));
            } else {
                logger.error("web log afterThrowing end,target method runtime exception:{}", JSONObject.toJSONString(responseLogInfo));
            }
        } catch (Exception e) {
            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e, RequestLogInfoContext.get()));
        }
    }

    private void setRequestLogInfo(RequestLogInfo requestLogInfo, JoinPoint joinPoint){
        HttpServletRequest request = this.getRequest();
        logger.debug("sessionId:"+request.getSession().getId());
        requestLogInfo.setIp(null == request ? null : this.getIp(request));
        requestLogInfo.setHost(null == request ? null : request.getRemoteHost());
        requestLogInfo.setPort(null == request ? null : request.getRemotePort());
        requestLogInfo.setUrl(null == request ? null : request.getRequestURL().toString());
        requestLogInfo.setUri(null == request ? null : request.getRequestURI());
        requestLogInfo.setClassName(this.getClassName(joinPoint));
        requestLogInfo.setMethodName(this.getMethodName(joinPoint));
        requestLogInfo.setRequestHeaderMap(this.getRequestHeaderMap());
        requestLogInfo.setParamMap(this.getParamMap(joinPoint));
    }

    private void setResponseLogInfo(ResponseLogInfo responseLogInfo){
        RequestLogInfo requestLogInfo = RequestLogInfoContext.get();
        HttpServletResponse response = this.getResponse();
        responseLogInfo.setIp(null == requestLogInfo ? null : requestLogInfo.getIp());
        responseLogInfo.setHost(null == requestLogInfo ? null : requestLogInfo.getHost());
        responseLogInfo.setPort(null == requestLogInfo ? null : requestLogInfo.getPort());
        responseLogInfo.setUrl(null == requestLogInfo ? null : requestLogInfo.getUrl());
        responseLogInfo.setUri(null == requestLogInfo ? null : requestLogInfo.getUri());
        responseLogInfo.setClassName(null == requestLogInfo ? null : requestLogInfo.getClassName());
        responseLogInfo.setMethodName(null == requestLogInfo ? null : requestLogInfo.getMethodName());
        responseLogInfo.setParamMap(null == requestLogInfo ? null : requestLogInfo.getParamMap());
        responseLogInfo.setRequestHeaderMap(null == requestLogInfo ? null : requestLogInfo.getRequestHeaderMap());
        responseLogInfo.setResponseHeaderMap(this.getResponseHeaderMap());
        responseLogInfo.setStatus(response.getStatus());
        responseLogInfo.setContentType(response.getContentType());
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

    private HttpServletRequest getRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null == attributes ? null : attributes.getRequest();
        return request;
    }

    private HttpServletResponse getResponse(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse request = null == attributes ? null : attributes.getResponse();
        return request;
    }

    private Map<String, String> getRequestHeaderMap() {
        HttpServletRequest request = this.getRequest();
        Map<String, String> headerMap = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (null == headerNames) {
            return headerMap;
        }
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headerMap.put(key, value);
        }
        return headerMap;
    }

    private Map<String, String> getResponseHeaderMap() {
        HttpServletResponse response = this.getResponse();
        Map<String, String> headerMap = new LinkedHashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        if (null == headerNames || headerNames.size() <=0) {
            return headerMap;
        }
        for (String headerName:headerNames) {
            String key = headerName;
            String value = response.getHeader(key);
            headerMap.put(key, value);
        }
        return headerMap;
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
            if(paramValues[i] instanceof ResourceHttpRequestHandler) { continue; }
            if(paramValues[i] instanceof WebContentGenerator) { continue; }
            if(paramValues[i] instanceof HttpRequestHandler) { continue; }
            result.put(paramNames[i], paramValues[i]);
        }
        return result;
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        logger.debug("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            logger.debug("Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            logger.debug("WL-Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            logger.debug("HTTP_CLIENT_IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            logger.debug("HTTP_X_FORWARDED_FOR ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            logger.debug("X-Real-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            logger.debug("getRemoteAddr ip: " + ip);
        }
        logger.debug("获取客户端ip: " + ip);
        return ip;
    }

}