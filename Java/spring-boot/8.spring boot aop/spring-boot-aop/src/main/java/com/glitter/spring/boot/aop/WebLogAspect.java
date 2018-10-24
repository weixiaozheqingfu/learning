package com.glitter.spring.boot.aop;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.context.MethodCallInfoContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 拦截器：记录用户操作日志，检查用户是否登录
 */
@Aspect
@Component
public class WebLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    /**
     * 定义拦截规则：拦截com.glitter.spring.boot.web.controller包下面的所有类中,有@RequestMapping注解的方法。
     */
    @Pointcut("execution(public * com.glitter.spring.boot.web.controller.*.*(..)) and @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void webLog(){}

    @Before("webLog()")
    public void before(JoinPoint joinPoint) throws Throwable {
        System.out.println(JSONObject.toJSONString(MethodCallInfoContext.get()));

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logger.info("-------------------用户发起请求-----------------");
        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        //如果是表单，参数值是普通键值对。如果是application/json，则request.getParameter是取不到的。
        logger.info("HTTP_HEAD Type : " + request.getHeader("Content-Type"));
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());

    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info("方法的返回值 : " + ret);
        logger.info("------------------请求结束------------------");
    }

    //后置异常通知
    @AfterThrowing(throwing = "ex", pointcut = "webLog()")
    public void throwss(JoinPoint jp, Exception ex){
        logger.info("方法异常时执行.....");
    }

    //后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
    @After("webLog()")
    public void after(JoinPoint jp){
        logger.info("方法最后执行.....");
    }




    /**
     * 拦截器具体实现
     * @param pjp
     * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。）
     */
    @Around("webLog()")
    public Object around(ProceedingJoinPoint pjp){
        System.out.println(JSONObject.toJSONString(MethodCallInfoContext.get()));

        long beginTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        //获取被拦截的方法
        Method method = signature.getMethod();
        //获取被拦截的方法名
        String methodName = method.getName();

        //保存所有请求参数，用于输出到日志中
        Set<Object> allParams1 = new LinkedHashSet<>();

        logger.info("请求开始，方法：{}", methodName);

        Object result = null;

        Object[] args = pjp.getArgs();

        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            logger.info("exception: ", e);
            result = new ResponseResult("-1", "发生异常："+e.getMessage());
        }

        return result;
    }


}