package com.glitter.spring.boot.web.interceptor;

import com.glitter.spring.boot.util.TemplateUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AemoInterceptor implements HandlerInterceptor {

    /**
     * 进入controller层之前拦截请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        System.out.println("AemoInterceptor preHandle...............................................");
        return true;
    }

    /**
     * 处理请求完成后视图渲染之前的处理操作
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView modelAndView) throws Exception {
        System.out.println("AemoInterceptor postHandle...............................................");
    }

    /**
     * 视图渲染之后的操作
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e) throws Exception {
        // 1.如果请求正常到达controller层,程序在controller层抛出异常,如果该异常被全局异常捕获,而全局异常处理没有继续抛出异常,而是正常返回数据,则此时相当于异常被全局异常吞掉,则能执行到该方法,且e=null,即没有异常信息。
        // 2.如果请求正常到达controller层,程序在controller层抛出异常,如果该异常被全局异常捕获,而全局异常处理记录日志后继续抛出异常,则能执行到该方法,且e异常信息可以打印,如果此方法没有捕获异常,
        //   则就能会继续往外抛,最后被spring默认异常处理机制捕获并打印出该异常,同时spring会请求/error的页面,同样会经过该拦截器,由于这个过程没有异常,此时到该方法e=null,然后/error的错误信息会返回给用户。
        System.out.println("AemoInterceptor afterCompletion...............................................");
        System.out.println("AemoInterceptor afterCompletion request path..............................................." + request.getRequestURI());
        System.out.println("AemoInterceptor afterCompletion exception..............................................." + TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e));
    }

}
