package com.glitter.spring.boot.aop.datasource.aspect;

import com.glitter.spring.boot.context.DatasourceContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DatasourceAspect implements Ordered {
    private static final Logger log = LoggerFactory.getLogger(DatasourceAspect.class);

    @Pointcut("execution(public * com.glitter.spring.boot.service..*(..)) && @annotation(com.glitter.spring.boot.aop.datasource.annotation.ReadDatasource)")
    public void datasourceAspectPointcut(){}

    @Around("datasourceAspectPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            DatasourceContext.set(DatasourceContext.READ);
            return joinPoint.proceed();
        } finally {
            DatasourceContext.remove();
            log.info("清除DatasourceContext");
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
