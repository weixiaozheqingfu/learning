package com.glitter.spring.boot.web.listener;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Servlet的监听器Listener，它是实现了javax.servlet.ServletContextListener 接口的
 * 服务器端程序，它也是随web应用的启动而启动，只初始化一次，随web应用的停止而销毁。主要作用是：做一些初始化
 * 的内容添加工作、设置一些基本的内容、比如一些参数或者是一些固定的对象等等。
 */
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
        String name = servletContextAttributeEvent.getName();
        String value = servletContextAttributeEvent.getValue().toString();
        System.out.println("Aemo ServletContext attributeAdded name:"+name+";value:"+value);
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
