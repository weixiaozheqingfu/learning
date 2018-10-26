package com.glitter.spring.boot.aop;

import com.glitter.spring.boot.exception.BusinessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *  所有方法齐全且没有任何异常的情况下,
    DemoAspect1.around调用前......................................................................
    DemoAspect1.before......................................................................
    DemoAspect1.around调用后......................................................................
    DemoAspect1.after......................................................................
    DemoAspect1.afterReturning......................................................................

    业务方法抛出异常,around吃掉异常的情况下,
    DemoAspect1.around调用前......................................................................
    DemoAspect1.before......................................................................
    DemoAspect1.around捕获异常不抛出的情况......................................................................
    DemoAspect1.after......................................................................
    DemoAspect1.afterReturning......................................................................

    业务方法抛出异常,around抛出异常的情况下,
    DemoAspect1.around调用前......................................................................
    DemoAspect1.before......................................................................
    DemoAspect1.around捕获异常抛出的情况......................................................................
    DemoAspect1.after......................................................................
    DemoAspect1.afterThrowing......................................................................

    上面几种情况其实完全符合aop的执行顺序,并且可以发现如果所有方法都存在的情况下,他们的执行顺序是
    【around调用前】->【before】->【around调用后（有异常看是否捕获决定是否执行）】->【after】->【afterReturning】或【afterThrowing】

    而我们的最佳实践是,如果使用了around,就不要使用before和afterReturning了,
    而是使用around,after,afterThrowing组合
    每个方法有异常就抛出即可.

 */
@Aspect
@Component
public class DemoAspect1 {
    private static final Logger logger = LoggerFactory.getLogger(DemoAspect1.class);

    @Pointcut("execution(public * com.glitter.spring.boot.web.controller.*.*(..)) and @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void webLogAspectPointcut(){}

    @Before("webLogAspectPointcut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        System.out.println("DemoAspect1.before......................................................................");
        if(1==1){
            throw new BusinessException("-1","before异常");
        }
    }

    @Around("webLogAspectPointcut()")
    public Object around(ProceedingJoinPoint pjp){
        try {
            System.out.println("DemoAspect1.around调用前......................................................................");
            if(1==1){
                throw new BusinessException("-1","around调用前异常");
            }
            Object result = pjp.proceed();
            System.out.println("DemoAspect1.around调用后......................................................................");
            if(1==1){
                throw new BusinessException("-1","around调用后异常");
            }
            return result;
        } catch (Throwable e) {
            System.out.println("DemoAspect1.around捕获异常抛出的情况......................................................................");
            // 最佳实践:要么此处组织返回结果,然后正常return result;
            // 要么此处继续往外抛异常,强烈推荐往外抛异常,返回结果由全局异常统一处理,然后决定如何返回,而不是在这里决定如何返回,aop这里不应该干预结果数据。
            // result = new ResponseResult("-1", "发生异常："+e.getMessage());
            throw new BusinessException("-1","参数异常了...");
        }
    }

    @After("webLogAspectPointcut()")
    public void after(JoinPoint jp){
        System.out.println("DemoAspect1.after......................................................................");
        if(1==1){
            throw new BusinessException("-1","after异常");
        }
    }

    @AfterReturning(returning = "ret", pointcut = "webLogAspectPointcut()")
    public void afterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
        System.out.println("DemoAspect1.afterReturning......................................................................");
        if(1==1){
            throw new BusinessException("-1","afterReturning异常");
        }
    }

    @AfterThrowing(throwing = "ex", pointcut = "webLogAspectPointcut()")
    public void afterThrowing(JoinPoint jp, Exception ex){
        System.out.println("DemoAspect1.afterThrowing......................................................................");
        if(1==1){
            throw new BusinessException("-1","afterThrowing异常");
        }
    }

}