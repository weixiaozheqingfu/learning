package com.glitter.spring.boot.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class DemoFilter implements Filter{

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("DemoFilter init......................");

        // 是单例的
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("DemoFilter doFilter......................");
        String initParam = filterConfig.getInitParameter("initParam");
        System.out.println("DemoFilter doFilter initParam "+ initParam +"......................");

        // 获取request,response
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        filterChain.doFilter(request, response);
        return;
    }

    @Override
    public void destroy() {
        System.out.println("DemoFilter destroy......................");
        this.filterConfig = null;
    }

}
