package com.glitter.spring.boot.service.aop;

import com.glitter.spring.boot.service.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Order(2)
public class TimeAspect {

    private static final Logger logger = LoggerFactory.getLogger(TimeAspect.class);

    @Before(pointcut = "accept")
    public void before(JoinPoint joinPoint) throws Throwable {
        logger.debug("TimeAspect.before.......................................");
    }

    @Around(pointcut = "accept")
    public Object around (JoinPoint joinPoint) throws Throwable {
        logger.debug("TimeAspect.around开始.......................................");
        Object result = joinPoint.proceed();
        logger.debug("TimeAspect.around完毕.......................................");
        logger.debug("TimeAspect.around结果......................................." + result.toString());
        return result;
    }

    @After(pointcut = "accept")
    public void after(JoinPoint joinPoint) throws Throwable {
        logger.debug("TimeAspect.after.......................................");
    }

    @AfterReturning(pointcut = "accept", returning = "ret")
    public void afterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
        logger.debug("TimeAspect.afterReturning.......................................");
        logger.debug("TimeAspect.afterReturning.ret......................................." + ret.toString());
    }

    @AfterThrowing(pointcut = "accept", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex) throws Exception {
        logger.debug("TimeAspect.afterThrowing.ex......................................." + ex.getMessage());
    }

}
