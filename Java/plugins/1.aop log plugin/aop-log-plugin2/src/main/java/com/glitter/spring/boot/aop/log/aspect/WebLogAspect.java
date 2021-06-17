package com.glitter.spring.boot.aop.log.aspect;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.aop.log.bean.RequestLogInfo;
import com.glitter.spring.boot.aop.log.bean.ResponseLogInfo;
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

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void webLogAspectPointcut(){}

    @Before("webLogAspectPointcut()")
    public void before(JoinPoint joinPoint) {
        try {
            RequestLogInfo requestLogInfo = new RequestLogInfo();
            this.setRequestLogInfo(requestLogInfo, joinPoint);
            logger.info("[" + requestLogInfo.getUri() + "]输入参数:{}", JSONObject.toJSONString(requestLogInfo.getParamMap()));
//          logger.info("[" + requestLogInfo.getUri() + "]输入参数:{}", JSONObject.toJSONString(requestLogInfo.getParamMap(), SerializerFeature.WriteMapNullValue));
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
//          throw e;
        }
    }

    @After("webLogAspectPointcut()")
    public void after(JoinPoint joinPoint){
        try {
            ResponseLogInfo responseLogInfo = new ResponseLogInfo();
            this.setResponseLogInfo(responseLogInfo, joinPoint);
            logger.info("[" + responseLogInfo.getUri() + "]执行完毕....................................................................");
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
//          throw e;
        }
    }

    @AfterReturning( pointcut = "webLogAspectPointcut()", returning = "ret")
    public void afterReturning(JoinPoint joinPoint, Object ret) {
        try {
            ResponseLogInfo responseLogInfo = new ResponseLogInfo();
            this.setResponseLogInfo(responseLogInfo, joinPoint, ret);
            logger.info("[" + responseLogInfo.getUri() + "]输出参数:{}", JSONObject.toJSONString(responseLogInfo.getReturnObj()));
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
//          throw e;
        }
    }

    @AfterThrowing(pointcut = "webLogAspectPointcut()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
        try {
            ResponseLogInfo responseLogInfo = new ResponseLogInfo();
            this.setResponseLogInfo(responseLogInfo, joinPoint);
            responseLogInfo.setEx(ex);
            responseLogInfo.setStatus(500);
            logger.info("[" + responseLogInfo.getUri() + "]异常信息:{}", JSONObject.toJSONString(responseLogInfo.getEx()));
        } catch (Throwable e) {
            logger.error(JSONObject.toJSONString(e));
//          throw e;
        }
    }

    private void setRequestLogInfo(RequestLogInfo requestLogInfo, JoinPoint joinPoint) {
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

    private void setResponseLogInfo(ResponseLogInfo responseLogInfo, JoinPoint joinPoint) {
        this.setResponseLogInfo(responseLogInfo, joinPoint, null);
    }

    private void setResponseLogInfo(ResponseLogInfo responseLogInfo, JoinPoint joinPoint, Object ret){
        HttpServletRequest request = this.getRequest();
        HttpServletResponse response = this.getResponse();
        responseLogInfo.setIp(null == request ? null : this.getIp(request));
        responseLogInfo.setHost(null == request ? null : request.getRemoteHost());
        responseLogInfo.setPort(null == request ? null : request.getRemotePort());
        responseLogInfo.setUrl(null == request ? null : request.getRequestURL().toString());
        responseLogInfo.setUri(null == request ? null : request.getRequestURI());
        responseLogInfo.setClassName(this.getClassName(joinPoint));
        responseLogInfo.setMethodName(this.getMethodName(joinPoint));
        responseLogInfo.setRequestHeaderMap(this.getRequestHeaderMap());
        responseLogInfo.setParamMap(this.getParamMap(joinPoint));
        responseLogInfo.setResponseHeaderMap(this.getResponseHeaderMap());
        responseLogInfo.setStatus(response.getStatus());
        responseLogInfo.setContentType(response.getContentType());
        responseLogInfo.setReturnObj(ret);
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
        String className = joinPoint.getTarget().getClass().getSimpleName();
        return className;
    }

    private HttpServletRequest getRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null == attributes ? null : attributes.getRequest();
        return request;
    }

    private HttpServletResponse getResponse(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = null == attributes ? null : attributes.getResponse();
        return response;
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