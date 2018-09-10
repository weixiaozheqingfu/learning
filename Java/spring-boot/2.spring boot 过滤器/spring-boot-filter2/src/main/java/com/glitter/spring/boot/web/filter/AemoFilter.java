package com.glitter.spring.boot.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AemoFilter implements Filter{

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AemoFilter init......................");

        // 是单例的
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("AemoFilter doFilter......................");
        String initParam = filterConfig.getInitParameter("initParam");
        System.out.println("AemoFilter doFilter initParam "+ initParam +"......................");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        filterChain.doFilter(request, response);
        return;
    }

    @Override
    public void destroy() {
        System.out.println("AemoFilter destroy......................");
        this.filterConfig = null;
    }

}
