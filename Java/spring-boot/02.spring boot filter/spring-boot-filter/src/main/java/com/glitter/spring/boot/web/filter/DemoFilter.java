package com.glitter.spring.boot.web.filter;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 勿乱是内置tomcat容器还是外置tomcat容器,使用@WebFilter注解的方式配置过滤器,如果有多个过滤器,是通过类名的名称来控制执行顺序的,如AemoFilter的doFilter方法总是比DemoFilter的doFilter方法先执行.
 * 即便org.springframework.core.annotation.Order注解也没有任何效果,所以当项目中有多个过滤器时,并不推荐使用注解方式配置过滤器
 * 另外容器初始化过滤器时的执行顺序无法控制,即init方法的执行先后顺序无法控制,这一点需要了解到。
 *
 * 对于doFilter方法的顺序,如果执意想使用注解方式,并且还想控制doFilter方法的执行顺序,可以使类名采用F0DemoFilter,F1AemoFilter的方式来控制加载顺序,但毕竟感觉还是不太好,建议项目中只有一个过滤器的时候使用这种注解的方式。
 */
@WebFilter(filterName = "demoFilter", urlPatterns = "/*", initParams = {
@WebInitParam(name = "loginUI", value = "/loginUI"),
@WebInitParam(name = "loginProcess", value = "home/login"),
@WebInitParam(name = "encoding", value = "utf-8")})
@Order(value = 2)
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
