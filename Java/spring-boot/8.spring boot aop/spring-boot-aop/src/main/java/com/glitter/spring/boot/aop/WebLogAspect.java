package com.glitter.spring.boot.aop;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.RequestLogInfo;
import com.glitter.spring.boot.bean.ResponseLogInfo;
import com.glitter.spring.boot.context.RequestLogInfoContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

@Aspect
@Component
public class WebLogAspect {

    @Pointcut("execution(public * com.glitter.spring.boot.web.controller..*(..)) and @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void demoAspectPointcut(){}

    @Before("demoAspectPointcut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        try {
            this.setRequestLogInfoContext(joinPoint);
            System.out.println("before.....................................................................");
            System.out.println(JSONObject.toJSONString(RequestLogInfoContext.get()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterReturning( pointcut = "demoAspectPointcut()", returning = "ret")
    public void afterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
        try {
            // System.out.println("afterReturning.............................................................ret:"+JSONObject.toJSONString(ret));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterThrowing(pointcut = "demoAspectPointcut()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex){
        // System.out.println("afterReturning.................................................................ex:"+JSONObject.toJSONString(ex));
    }

    @After("demoAspectPointcut()")
    public void after(JoinPoint joinPoint){
        System.out.println("afterReturning.................................................................");
        System.out.println(JSONObject.toJSONString(this.getResponseLogInfo()));
    }


    private void setRequestLogInfoContext(JoinPoint joinPoint){
        RequestLogInfo requestLogInfo = new RequestLogInfo();
        HttpServletRequest request = this.getRequest();
        System.out.println("sessionId:"+request.getSession().getId());
        requestLogInfo.setIp(null == request ? null : this.getIp(request));
        requestLogInfo.setHost(null == request ? null : request.getRemoteHost());
        requestLogInfo.setPort(null == request ? null : request.getRemotePort());
        requestLogInfo.setUrl(null == request ? null : request.getRequestURL().toString());
        requestLogInfo.setUri(null == request ? null : request.getRequestURI());
        requestLogInfo.setClassName(this.getClassName(joinPoint));
        requestLogInfo.setMethodName(this.getMethodName(joinPoint));
        requestLogInfo.setHeaderMap(this.getRequestHeaderMap());
        requestLogInfo.setParamMap(this.getParamMap(joinPoint));
        RequestLogInfoContext.set(requestLogInfo);
    }

    private ResponseLogInfo getResponseLogInfo(){
        ResponseLogInfo responseLogInfo = new ResponseLogInfo();

        RequestLogInfo requestLogInfo = RequestLogInfoContext.get();
        HttpServletResponse response = this.getResponse();

        requestLogInfo.setIp(null == requestLogInfo ? null : requestLogInfo.getIp());
        responseLogInfo.setHost(null == requestLogInfo ? null : requestLogInfo.getHost());
        responseLogInfo.setPort(null == requestLogInfo ? null : requestLogInfo.getPort());
        responseLogInfo.setUrl(null == requestLogInfo ? null : requestLogInfo.getUrl());
        responseLogInfo.setUri(null == requestLogInfo ? null : requestLogInfo.getUri());
        responseLogInfo.setClassName(null == requestLogInfo ? null : requestLogInfo.getUri());
        responseLogInfo.setMethodName(null == requestLogInfo ? null : requestLogInfo.getUri());
        responseLogInfo.setParamMap(null == requestLogInfo ? null : requestLogInfo.getParamMap());
        responseLogInfo.setHeaderMap(this.getResponseHeaderMap());
        responseLogInfo.setStatus(response.getStatus());
        responseLogInfo.setClassName(response.getContentType());

        // TODO returnValue  ex  也到放置在ResponseLogInfoContext中  在对应的方法放上值，
        // 此方法也先从ResponseLogInfoContext中获取ResponseLogInfo对象

        return responseLogInfo;
    }

    private MethodSignature getMethodSignature(JoinPoint joinPoint){
        return (MethodSignature) joinPoint.getSignature();
    }

    private Method getMethod(JoinPoint joinPoint){
        return this.getMethodSignature(joinPoint).getMethod();
    }

    private String getMethodName(JoinPoint joinPoint){
        // 这种方式也可以
        // String methodName1 = joinPoint.getSignature().getName();
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
            result.put(paramNames[i], paramValues[i]);
        }
        return result;
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        // System.out.println("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            // System.out.println("Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            // System.out.println("WL-Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            // System.out.println("HTTP_CLIENT_IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            // System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            // System.out.println("X-Real-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            // System.out.println("getRemoteAddr ip: " + ip);
        }
        // System.out.println("获取客户端ip: " + ip);
        return ip;
    }

    private Logger getLogger(JoinPoint joinPoint){
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        return logger;
    }

}