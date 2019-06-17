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
        logger.debug("LogAspect.before.......................................");
    }

    @Around(pointcut = "accept")
    public Object around (JoinPoint joinPoint) throws Throwable {
        logger.debug("LogAspect.around开始.......................................");
        Object result = joinPoint.proceed();
        logger.debug("LogAspect.around完毕.......................................");
        logger.debug("LogAspect.around结果......................................." + result);
        return result;
    }

    @After(pointcut = "accept")
    public void after(JoinPoint joinPoint) throws Throwable {
        logger.debug("LogAspect.after.......................................");
    }

    @AfterReturning(pointcut = "accept", returning = "ret")
    public void afterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
        logger.debug("LogAspect.afterReturning.......................................");
        logger.debug("LogAspect.afterReturning.ret......................................." + ret);
    }

    @AfterThrowing(pointcut = "accept", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex) throws Exception {
        logger.debug("LogAspect.afterThrowing.ex......................................." + ex.getMessage());
    }

}
