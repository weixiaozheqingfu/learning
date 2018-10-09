package com.glitter.spring.boot.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.util.TemplateUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AemoInterceptor implements HandlerInterceptor {

    /**
     * 进入controller层之前拦截请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        System.out.println("AemoInterceptor preHandle...............................................");
        // 被任意一个拦截器的preHandle方法抛出异常,则后续的所有方法或后续的拦截器都不会再执行,抛出的异常会被全局异常捕获
        // 此时如果全局异常选择正常返回,则请求方可以正常收到返回信息,如果全局异常处理选择继续抛出异常,
        // 则被spring默认异常处理机制捕获并打印出该异常,同时spring会请求/error的页面,同样会经过该拦截器,由于这个过程没有异常,此时到该方法e=null,然后/error的错误信息会返回给用户。
        if(1==1){
            throw new BusinessException("-1", "用户未登录");
        }
        if(1==1){
            // 被任意一个拦截器的preHandle方法return false,则后续的所有方法或后续的拦截器都不会再执行
            // 当然如果只是简单的return false,则请求方不会有任何响应,通常在return false 之前会给客户端一个相应。
            this.response(request,response,"-1","系统异常");
            return false;
        }
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







    /**
     * 是否是ajax请求
     *
     * @param request
     * @return
     */
    private boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        String xRequestedWith = request.getHeader("X-Requested-With");

        boolean flag1 = accept != null && accept.indexOf("application/json") != -1;
        boolean flag2 = xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1;
        boolean isAjax = flag1 || flag2;
        return isAjax;
    }

    /**
     * 返回响应数据
     *
     * 测试的话可以是postman模拟ajax,只要在postman的header头中加入X-Requested-With属性,值为XMLHttpRequest即可.
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param code
     * @param msg
     * @return
     * @throws IOException
     */
    private void response(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String code, String msg) throws IOException {
        if (isAjaxRequest(httpServletRequest)) {
            httpServletRequest.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("text/html;charset=utf-8");
            ResponseResult result = new ResponseResult(code, msg);
            httpServletResponse.getWriter().write(JSONObject.toJSONString(result));
            return;
        }
        String url = "http://www.baidu.com";
        httpServletResponse.sendRedirect(url);
    }


}
