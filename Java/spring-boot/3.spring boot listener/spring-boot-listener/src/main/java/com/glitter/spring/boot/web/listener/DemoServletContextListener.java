package com.glitter.spring.boot.web.listener;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * Servlet的监听器Listener，它是实现了javax.servlet.ServletContextListener 接口的
 * 服务器端程序，它也是随web应用的启动而启动，只初始化一次，随web应用的停止而销毁。主要作用是：做一些初始化
 * 的内容添加工作、设置一些基本的内容、比如一些参数或者是一些固定的对象等等。
 *
 * 使用@WebListener注解,如果有多个过滤器,顺序无法控制,org.springframework.core.annotation.Order也不可以
 * 使用@WebListener注解,如何配置类似web.xml中配置的初始化参数<context-param>,暂时没有找到配置的地方,可能是不需要吧,需要什么属性直接在代码里写就好了。
 *
 * 使用内置tomcat容器与外置tomcat容器ServletContextListener和ServletContextAttributeListener的执行顺序还是存在差别的,这一点请注意.
 * 内置tomcat容器先执行的是ServletContextAttributeListener.attributeAdded.
 * 外置tomcat容器先执行的是ServletContextListener.contextInitialized.
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
        String name = servletContextAttributeEvent.getName();
        String value = servletContextAttributeEvent.getValue().toString();
        System.out.println("Demo ServletContext attributeAdded name:"+name+";value:"+value);
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
