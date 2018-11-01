package com.glitter.spring.boot.exception;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.util.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseResult handleBusinessException(BusinessException e) {
        try {
            // 如果有需要的话,这里也可以将参数打印出来RequestLogInfoContext.get()
            logger.error("GlobalExceptionHandler.handleBusinessException 捕获业务异常信息:{}", JSONObject.toJSONString(e));
            return new ResponseResult(e.getCode(), e.getMessage());
        } catch (Exception ex) {
            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), ex));
            return new ResponseResult(e.getCode(), e.getMessage());
        }
    }

    // 举例,比如项目代码中有一段远程调用的代码,那么有两种处理方式:
    // 1.对这行代码单独try catch,catch中记录异常信息,然后抛出一个业务异常,那么最后用户可以看到这个异常信息.
    // 2.对这行代码不做任何处理,有异常直接层层抛出,那么aop会记录异常日志,这里的全局异常也会记录异常日志,用户看到的异常信息是全局异常这里的"系统异常"
    // 至于用上面那种情况处理,看业务需要了,如果想让用户看到具体的运行错误信息,就用第一种,如果不想让用户看到具体的运行错误信息,就用第二种,都行。

    // 另外,有了aop日志拦截和全局异常拦截,项目代码有两点改变.
    // 1.所有的controller方法和service方法都不需要专门写try catch了,除非有特定的需要,可以写。
    // 2.所有的controller方法和service方法方法都不用写出入参的日志了,只有方法局部有需要输出的日志,可以写,比如有远程调用时的调用前入参和调用后的出参日志等,以及涉及到代码拍错分析的日志可能需要打印等。

    @ExceptionHandler(Exception.class)
    public ResponseResult handleException(Exception e) {
        try {
            logger.error("GlobalExceptionHandler.handleException 捕获运行异常信息:{}", JSONObject.toJSONString(e));
            return new ResponseResult("-1", "系统异常");
        } catch (Exception ex) {
            logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), ex));
            return new ResponseResult("-1", "系统异常");
        }
    }

}