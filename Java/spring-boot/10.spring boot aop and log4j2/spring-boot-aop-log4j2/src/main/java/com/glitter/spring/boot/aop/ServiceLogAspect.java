package com.glitter.spring.boot.aop;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.RequestLogInfo;
import com.glitter.spring.boot.bean.ResponseLogInfo;
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
 * @author Administrator
 */
@Aspect
@Component
public class ServiceLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(public * com.glitter.spring.boot.service..*(..)) and @annotation(org.springframework.stereotype.Service)")
    public void demoAspectPointcut(){}

    @Before("demoAspectPointcut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        try {
            // if(1==1){
            //    throw new BusinessException("-2","before出错了");
            // }
            logger.info("before begin....................................................................");
            RequestLogInfo requestLogInfo = null == RequestLogInfoContext.get() ? new RequestLogInfo() : RequestLogInfoContext.get();
            this.setRequestLogInfo(requestLogInfo, joinPoint);
            logger.info("before,请求地址:{}", requestLogInfo.getUri());
            if(null == ResponseLogInfoContext.get()){
                RequestLogInfoContext.set(requestLogInfo);
            }
            logger.info("before end,输入参数:{}", JSONObject.toJSONString(requestLogInfo));
        } catch (Exception e) {
            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e, RequestLogInfoContext.get()));
            throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
        }
    }

    @After("demoAspectPointcut()")
    public void after(JoinPoint joinPoint){
        try {
            logger.info("after begin....................................................................");
            ResponseLogInfo responseLogInfo = null == ResponseLogInfoContext.get() ? new ResponseLogInfo() : ResponseLogInfoContext.get();
            this.setResponseLogInfo(responseLogInfo);
            if(null == ResponseLogInfoContext.get()){
                ResponseLogInfoContext.set(responseLogInfo);
            }
            logger.info("after end....................................................................");
        } catch (Exception e) {
            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e, RequestLogInfoContext.get()));
            throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
        }
    }

    @AfterReturning( pointcut = "demoAspectPointcut()", returning = "ret")
    public void afterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
        try {
            logger.info("afterReturning begin...........................................................");
            ResponseLogInfo responseLogInfo = null == ResponseLogInfoContext.get() ? new ResponseLogInfo() : ResponseLogInfoContext.get();
            responseLogInfo.setReturnObj(ret);
            if(null == ResponseLogInfoContext.get()){
                ResponseLogInfoContext.set(responseLogInfo);
            }
            logger.info("afterReturning end,输出参数:{}", JSONObject.toJSONString(responseLogInfo));
        } catch (Exception e) {
            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e, RequestLogInfoContext.get()));
            throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
        }
    }

    @AfterThrowing(pointcut = "demoAspectPointcut()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex){
        try {
            logger.error("afterThrowing begin............................................................");
            ResponseLogInfo responseLogInfo = null == ResponseLogInfoContext.get() ? new ResponseLogInfo() : ResponseLogInfoContext.get();
            responseLogInfo.setEx(ex);
            // 如果业务方法或者本aop方法有异常,异常没有做捕获处理,而是继续往外抛,包括到全局异常都没有捕获处理,而是继续往抛,最终会抛到spring boot默认异常处理器,那么response的status值会被改写,通常是500,也可能是其他值。
            // 所以用户最终看到的响应消息的结果如果是spring boot最后的默认异常处理返回的,其status就是spring boot赋予的。
            // 当然用户看到最后数据要么是我们经过异常捕获处理后返回的数据,要么是spring boot默认异常处理后返回的数据,如果是后者,用户是可以看到status值的.
            // 如果是后者,那么对于开发者在日志中看到的responseLogInfo的status值与spring boot 返回前端页面的status值可能是不一样的,这没有关系,了解到这一点就好,免得在这哪怕纠结一会儿也是浪费时间.
            responseLogInfo.setStatus(500);
            if(null == ResponseLogInfoContext.get()){
                ResponseLogInfoContext.set(responseLogInfo);
            }
            if(ex instanceof BusinessException){
                logger.error("afterThrowing end,目标方法业务异常:{}", JSONObject.toJSONString(responseLogInfo));
            } else {
                logger.error("afterThrowing end,目标方法运行异常:{}", JSONObject.toJSONString(responseLogInfo));
            }

            // if(1==1){
            //    throw new BusinessException("-2","出错了");
            // }
        } catch (Exception e) {
            // afterThrowing方法自己执行期异常只在此记录即可.
            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e, RequestLogInfoContext.get()));
            // afterThrowing方法自己执行期如果有异常不往外抛,要让代码抛出去目标方法的异常,否则,我的抛的异常会覆盖掉目标方法的异常.用户看到的就是afterThrowing方法的异常,afterThrowing方法自己执行期异常只在此记录即可.
            // throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
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
        // 如果业务方法或者本aop方法有异常,异常没有做捕获处理,而是继续往外抛,包括到全局异常都没有捕获处理,而是继续往抛,最终会抛到spring boot默认异常处理器,那么response的status值会被改写,通常是500。
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