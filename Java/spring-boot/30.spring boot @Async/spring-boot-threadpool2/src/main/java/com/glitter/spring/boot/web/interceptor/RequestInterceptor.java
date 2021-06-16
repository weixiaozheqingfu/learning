package com.glitter.spring.boot.web.interceptor;

import com.glitter.spring.boot.context.ContextManager;
import com.glitter.spring.boot.context.RequestContext;
import com.glitter.spring.boot.context.ResponseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        ContextManager.removeAllContext();
        ResponseContext.set(httpServletResponse);
        RequestContext.set(httpServletRequest);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 无论是否有异常,无论是系统异常还是业务异常,这里都会执行
     *
     * 如果有全局异常捕获做了异常处理,相当于在本次线程的流转过程中,将异常处理了,那走完这个步骤就是正常的返回。
     *
     * 如果没有全局异常捕获异常,相当于异常一直在往外抛,但是这里的代码也会被调用到,然后异常继续往外抛,最后给调用方返回的就是最后封装的500系统错误信息。
     *
     *
     * try{
     *     xxx
     * } catch (){
     *
     * } finall{
     *     RequestInterceptor.afterCompletion();
     * }
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        logger.info("RequestInterceptor.afterCompletion begin............................................");
        ContextManager.removeAllContext();
        logger.info("RequestInterceptor.afterCompletion end............................................");
    }

}
