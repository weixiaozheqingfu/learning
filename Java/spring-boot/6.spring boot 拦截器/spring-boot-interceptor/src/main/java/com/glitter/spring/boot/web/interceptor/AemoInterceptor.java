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
        // 1.果请求正常到达controller层,程序在controller层抛出异常,如果该异常被全局异常捕获,而全局异常处理没有继续抛出异常,而是正常返回数据,则此时相当于异常被全局异常吞掉,所以能执行到该方法,并且e=null,即没有异常信息。
        System.out.println("AemoInterceptor afterCompletion...............................................");
        System.out.println("AemoInterceptor afterCompletion request path..............................................." + request.getRequestURI());
        System.out.println("AemoInterceptor afterCompletion exception..............................................." + TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e));
    }

}
