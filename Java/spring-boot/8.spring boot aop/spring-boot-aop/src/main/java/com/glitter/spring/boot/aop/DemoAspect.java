package com.glitter.spring.boot.aop;

import com.glitter.spring.boot.exception.BusinessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * // TODO 多个AOP的之间的执行顺序?
 *
 * 建议使用before和AfterReturning,AfterThrowing,After配合使用,逐一非常清晰的满足所有情况的流转.
 * 不建议使用around,执行时机和AfterReturning,AfterThrowing,After配合使用时,有时候会掰扯不请,不找这个麻烦和不痛快.
 *
 * 使用around通知的场景:在多线程环境下,在joinpoint调用目标方法后需要使用调用之前的方法局部变量是可以的。
 * 但如果使用Before和After就不行,因为是两个方法,如果在切面类里面创建一个存储共享信息的成员变量field,则又不是线程安全的。
 * 但如果使用Before和After可以使用threadLocal解决共享变量问题。一般这种场景很少见。
 *
 * 最佳实践:
 * 1.每个通知方法有异常就抛出,异常由全局异常进行统一处理.
 * 2.在一个切面类中,要么是before,after,afterReturning,afterThrowing组合使。要么是around,after,afterThrowing组合.
 * 3.只要线程调用链能进入aop方法,无论是一切正常,还是目标方法抛出异常,还是某个通知方法抛出异常,after方法都一定会被执行。afterReturning,afterThrowing看调用链到此方法之前是否有异常执行其一。
 * 4.如果有多个aop类,且拦截规则有交集,则尽量统一用一种组合,不要aop1用before组合,而aop2用around组合,这样很可能aop1的before方法并不执行。所以我们就研究多个aop时,都使用before组合的情况,其他情况不研究.
 *
 * 5.上面总结都是废话,只要搞清楚调用链顺序,一切都是按规矩出牌的。
 *
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
        if(1==2){
            throw new BusinessException("-1","before异常");
        }
    }

    /**
     * 目标方法调用后,不管目标方法是抛出异常或者正常执行完毕返回数据都会先执行该方法,然后再去执行AfterReturning或者AfterThrowing方法
     * @param joinPoint
     */
    @After("demoAspectPointcut()")
    public void after(JoinPoint joinPoint){
        System.out.println("DemoAspect.after.......................................................................");
        if(1==2){
            throw new BusinessException("-1","after异常");
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
        System.out.println("DemoAspect.afterReturning..............................................................");
        if(1==2){
            throw new BusinessException("-1","afterReturning异常");
        }
    }

    /**
     * 目标方法抛出异常时执行
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(pointcut = "demoAspectPointcut()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex) throws Exception {
        System.out.println("DemoAspect.afterThrowing...............................................................");
        if(null != ex){
            throw ex;
        }
        if(1==1){
            throw new BusinessException("-1","afterThrowing异常");
        }
    }

    private Logger getLogger(JoinPoint joinPoint){
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        return logger;
    }

}