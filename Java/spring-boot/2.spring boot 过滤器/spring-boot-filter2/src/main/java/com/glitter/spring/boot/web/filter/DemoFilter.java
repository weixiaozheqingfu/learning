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

        // 获取request,response
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取配置参数
        String loginUI = filterConfig.getInitParameter("loginUI");
        String loginProcess = filterConfig.getInitParameter("loginProcess");
        String encoding = filterConfig.getInitParameter("encoding");

        // 设置请求的字符集(post请求方式有效)
        request.setCharacterEncoding(encoding);

        // 不带http://域名:端口的地址
        String uri = request.getRequestURI();
        if (uri.contains(loginUI) || uri.contains(loginProcess)) {
            // 请求的登录，放行
            filterChain.doFilter(request, response);
        } else {
            if (request.getSession().getAttribute("user") == null) {
                // 重定向到登录页面
                response.sendRedirect(request.getContextPath() + loginUI);
            } else {
                // 已经登录，放行
                filterChain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println("DemoFilter destroy......................");

        this.filterConfig = null;
    }

}
