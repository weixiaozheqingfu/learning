package com.glitter.spring.boot.aop;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.RequestLogInfo;
import com.glitter.spring.boot.bean.ResponseLogInfo;
import com.glitter.spring.boot.context.RequestLogInfoContext;
import com.glitter.spring.boot.context.ResponseLogInfoContext;
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

    /**
     * 定义拦截规则：拦截com.glitter.spring.boot.web.controller包下面的所有类中,有@RequestMapping注解的方法。
     */
    @Pointcut("execution(public * com.glitter.spring.boot.web.controller..*(..)) and @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void demoAspectPointcut(){}

    /**
     * 目标方法执行之前调用
     * @param joinPoint
     * @throws Throwable
     */
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

    /**
     * 目标方法执行完毕后正常返回数据时执行
     * @param joinPoint
     * @param ret
     * @throws Throwable
     */
    @AfterReturning( pointcut = "demoAspectPointcut()", returning = "ret")
    public void afterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
        try {
            System.out.println("afterReturning.............................................................ret:"+JSONObject.toJSONString(ret));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 目标方法抛出异常时执行
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(pointcut = "demoAspectPointcut()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex){
        System.out.println("afterReturning.................................................................ex:"+JSONObject.toJSONString(ex));
    }

    /**
     * 目标方法调用后,不管目标方法是抛出异常或者正常执行完毕返回数据最后都会执行该通知方法,该通知为后置最终通知,final增强
     * @param joinPoint
     */
    @After("demoAspectPointcut()")
    public void after(JoinPoint joinPoint){
        System.out.println("afterReturning.................................................................");
        System.out.println(JSONObject.toJSONString(ResponseLogInfoContext.get()));
    }

    private void setRequestLogInfoContext(JoinPoint joinPoint){
        RequestLogInfo requestLogInfo = new RequestLogInfo();
        HttpServletRequest request = this.getRequest();
        System.out.println("session:"+request.getSession());
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

    private ResponseLogInfo getResponseLogInfo(JoinPoint joinPoint){
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
        System.out.println("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            System.out.println("Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            System.out.println("WL-Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            System.out.println("HTTP_CLIENT_IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            System.out.println("X-Real-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            System.out.println("getRemoteAddr ip: " + ip);
        }
        System.out.println("获取客户端ip: " + ip);
        return ip;
    }

    private Logger getLogger(JoinPoint joinPoint){
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        return logger;
    }

}