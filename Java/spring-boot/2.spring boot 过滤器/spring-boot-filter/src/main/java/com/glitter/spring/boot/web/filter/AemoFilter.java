package com.glitter.spring.boot.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 使用@WebFilter注解的方式配置过滤器,如果有多个过滤器,是通过类名的名称来控制执行顺序的,如AemoFilter的doFilter方法总是比DemoFilter的doFilter方法先执行
 * 但是容器初始化过滤器时的执行顺序无法控制,即init方法的执行先后顺序无法控制
 */
@WebFilter(filterName = "aemoFilter", urlPatterns = "/*", initParams = {
@WebInitParam(name = "loginUI", value = "/loginUI"),
@WebInitParam(name = "loginProcess", value = "home/login"),
@WebInitParam(name = "encoding", value = "utf-8")})
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

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("AemoFilter destroy......................");
    }

}
