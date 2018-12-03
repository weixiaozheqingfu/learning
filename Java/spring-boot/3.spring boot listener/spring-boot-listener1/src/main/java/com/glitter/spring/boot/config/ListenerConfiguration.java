package com.glitter.spring.boot.config;

import com.glitter.spring.boot.web.listener.AemoServletContextListener;
import com.glitter.spring.boot.web.listener.DemoServletContextListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 无论内置tomcat容器还是外置tomcat容器,都可以通过servletListenerRegistrationBean.setOrder()方法控制多个listener的加载和监听顺序.
 * 推荐使用这种方式,而非使用注解方式。
 */
@Configuration
public class ListenerConfiguration implements WebMvcConfigurer {

    @Bean(name = "aemoServletContextListener")
    public AemoServletContextListener aemoServletContextListener() {
        return new AemoServletContextListener();
    }

    @Bean(name = "demoServletContextListener")
    public DemoServletContextListener demoServletContextListener() {
        return new DemoServletContextListener();
    }

    @Bean
    public ServletListenerRegistrationBean aemoServletListenerRegistrationBean() {
        // spring boot listener注册器对象
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
        // 设置监听器
        servletListenerRegistrationBean.setListener(aemoServletContextListener());
        // 设置执行顺序(值越小,越先执行)
        servletListenerRegistrationBean.setOrder(ServletListenerRegistrationBean.HIGHEST_PRECEDENCE + 1);
        // 返回spring boot listener注册器对象
        return servletListenerRegistrationBean;
    }

    @Bean
    public ServletListenerRegistrationBean demoServletListenerRegistrationBean() {
        // spring boot listener注册器对象
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
        // 设置监听器
        servletListenerRegistrationBean.setListener(demoServletContextListener());
        // 设置执行顺序(值越小,越先执行)
        servletListenerRegistrationBean.setOrder(ServletListenerRegistrationBean.HIGHEST_PRECEDENCE);
        // 返回spring boot listener注册器对象
        return servletListenerRegistrationBean;
    }

}
