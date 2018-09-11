package com.glitter.spring.boot.web.listener;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;

/**
 * 使用@WebListener注解,如果有多个过滤器,顺序控制???
 * 如何获取类似web.xml中配置的初始化参数<context-param>???
 */
@WebListener
public class DemoServletContextListener implements ServletContextListener,ServletContextAttributeListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Demo ServletContext initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Demo ServletContext destroyed");
    }

    @Override
    public void attributeAdded(ServletContextAttributeEvent servletContextAttributeEvent) {
        System.out.println("Demo ServletContext attributeAdded");
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent servletContextAttributeEvent) {
        System.out.println("Demo ServletContext attributeRemoved");
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent servletContextAttributeEvent) {
        System.out.println("Demo ServletContext attributeReplaced");
    }

}
