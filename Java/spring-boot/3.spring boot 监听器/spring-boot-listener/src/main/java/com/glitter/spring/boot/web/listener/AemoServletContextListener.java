package com.glitter.spring.boot.web.listener;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;

/**
 * 使用@WebListener注解,如果有多个过滤器,顺序无法控制,org.springframework.core.annotation.Order也不可以
 * 使用@WebListener注解,如何配置类似web.xml中配置的初始化参数<context-param>,暂时没有找到配置的地方,可能是不需要吧,需要什么属性直接在代码里写就好了。
 */
@WebListener
public class AemoServletContextListener implements ServletContextListener,ServletContextAttributeListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Aemo ServletContext initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Aemo ServletContext destroyed");
    }

    @Override
    public void attributeAdded(ServletContextAttributeEvent servletContextAttributeEvent) {
        System.out.println("Aemo ServletContext attributeAdded");
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent servletContextAttributeEvent) {
        System.out.println("Aemo ServletContext attributeRemoved");
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent servletContextAttributeEvent) {
        System.out.println("Aemo ServletContext attributeReplaced");
    }

}
