package com.glitter.spring.boot.aop;

import com.glitter.spring.boot.common.ResponseResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 示例AOP切面
 * 不建议使用该示例中的around通知,建议使用DemoAspect中的before通知.
 */
@Aspect
@Component
public class DemoAspect1 {
    private static final Logger logger = LoggerFactory.getLogger(DemoAspect1.class);

    @Pointcut("execution(public * com.glitter.spring.boot.web.controller.*.*(..)) and @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void webLogAspectPointcut(){}

    @Before("webLogAspectPointcut()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {

    }

    @AfterReturning(returning = "ret", pointcut = "webLogAspectPointcut()")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
    }


    @AfterThrowing(throwing = "ex", pointcut = "webLogAspectPointcut()")
    public void throwss(JoinPoint jp, Exception ex){
    }


    @After("webLogAspectPointcut()")
    public void after(JoinPoint jp){

    }

    /**
     * 拦截器具体实现
     * @param pjp
     * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。）
     */
    @Around("webLogAspectPointcut()")
    public Object around(ProceedingJoinPoint pjp){
        Object result = null;
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            logger.info("exception: ", e);
            result = new ResponseResult("-1", "发生异常："+e.getMessage());
        }

        return result;
    }


}