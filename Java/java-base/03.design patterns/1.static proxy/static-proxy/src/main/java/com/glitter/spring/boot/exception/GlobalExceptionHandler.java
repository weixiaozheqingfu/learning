package com.glitter.spring.boot.exception;

import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.util.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 使用@ControllerAdvice或@RestControllerAdvice处理全局异常有一定的局限性.只有进入Controller层的错误,才会由此全局异常类处理。
 * 拦截器抛出的异常,以及访问错误地址等情况,该全局异常类处理不了,由 Spring Boot默认的异常处理机制处理。
 *
 * 全局异常拦截主要用于不同的异常类,可以返回不同的数据到前端,具体方法内部如果没有特别需要就可以省略掉try catch了,直管抛出异常即可(不同异常返回值不同交给全局异常处理,异常日志记录交给aop日志来记录)。
 * 至于异常日志的记录最好是aop日志进行记录,因为aop记录的日志可以很容易将方法的输入参数打印出来,
 * 当然这里也可以进行日志打印,可以作为aop没有拦截到的方法的日志补充.
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseResult handleBusinessException(BusinessException e) {
        logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e));
        System.out.println(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e));
        return new ResponseResult(e.getCode(), e.getMessage(), null);
//        throw e;
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult handleException(Exception e) {
        logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e));
        System.out.println(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e));
        return new ResponseResult(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常", null);
    }

}