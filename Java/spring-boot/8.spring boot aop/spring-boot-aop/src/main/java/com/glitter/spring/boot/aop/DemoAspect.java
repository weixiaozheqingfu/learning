package com.glitter.spring.boot.aop;

import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
        System.out.println("DemoAspect.before......................................................................");
    }

    /**
     * 目标方法调用后,不管目标方法是抛出异常或者正常执行完毕返回数据都会先执行该方法,然后再去执行AfterReturning或者AfterThrowing方法
     * @param joinPoint
     */
    @After("demoAspectPointcut()")
    public void after(JoinPoint joinPoint){
        System.out.println("DemoAspect.after.......................................................................");
    }

    /**
     * 目标方法执行完毕后正常返回数据时执行
     * @param joinPoint
     * @param ret
     * @throws Throwable
     */
    @AfterReturning( pointcut = "demoAspectPointcut()", returning = "ret")
    public void afterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
        System.out.println("DemoAspect.afterReturning..............................................................");
    }

    /**
     * 目标方法抛出异常时执行
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(pointcut = "demoAspectPointcut()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex){
        System.out.println("DemoAspect.afterThrowing...............................................................");
    }

    private Logger getLogger(JoinPoint joinPoint){
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        return logger;
    }

}