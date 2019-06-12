package com.glitter.spring.boot.service.aop;

import com.glitter.spring.boot.service.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Order(1)
public class LogAspect{

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before(pointcut = "accept")
    public void before(JoinPoint joinPoint) throws Throwable {

    }

    @Around(pointcut = "accept")
    public Object around (JoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        return result;
    }

    @After(pointcut = "accept")
    public void after(JoinPoint joinPoint) throws Throwable {

    }

    @AfterReturning(pointcut = "accept", returning = "ret")
    public void afterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
        logger.info("DemoAspect1.afterReturning,ret:{}.................................",ret);
    }

    @AfterThrowing(pointcut = "accept()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex) throws Exception {
        logger.info("DemoAspect1.afterThrowing,ex:{}...............................................................",ex);
    }

}
