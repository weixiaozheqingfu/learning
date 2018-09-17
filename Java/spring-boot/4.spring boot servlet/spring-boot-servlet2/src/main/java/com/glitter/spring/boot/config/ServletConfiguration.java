package com.glitter.spring.boot.config;

import com.glitter.spring.boot.web.servlet.AemoServlet;
import com.glitter.spring.boot.web.servlet.DemoServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 推荐使用这种方式,而非使用注解方式,这样三方jar包的servlet也很容易可以集成进来。
 *
 * doGet方法和doPost方法,匹配到哪个就访问哪个,这中间有一个匹配度的问题,哪个更匹配就访问哪个,还有一个远近的问题,优先使用本项目的servlet,然后才是jar包中的。
 *
 * springmvn的servlet拦截的是/*,但是如果本项目中有servelt拦截/*,则优先级高于springmvc的.
 * 本项目中没有servlet拦截/*,则优先匹配本项目中特定的拦截路径的servlet,匹配不到会去springmvc匹配/*,springmvc的/*中做了分发,如果依然匹配不到springmvc会报404.
 */
@Configuration
public class ServletConfiguration implements WebMvcConfigurer {

    @Bean(name = "aemoServlet")
    public AemoServlet aemoServlet() {
        return new AemoServlet();
    }

    @Bean(name = "demoServlet")
    public DemoServlet demoServlet() {
        return new DemoServlet();
    }

    @Bean
    public ServletRegistrationBean aemoServletRegistrationBean() {
        // spring boot servlet注册器对象
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        // 设置servlet
        servletRegistrationBean.setServlet(aemoServlet());
        // 设置规则
        // servletRegistrationBean.addUrlMappings("/*");
        servletRegistrationBean.addUrlMappings("/aemo/*");
        // 设置加载顺序,即init方法加载顺序(值越小,越先执行,项目启动时加载需要值>0,值如果<0,则请求时才会加载)
        servletRegistrationBean.setLoadOnStartup(2);
        // 返回spring boot listener注册器对象
        return servletRegistrationBean;
    }

    @Bean
    public ServletRegistrationBean demoServletRegistrationBean() {
        // spring boot servlet注册器对象
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setEnabled(true);
        // 设置servlet
        servletRegistrationBean.setServlet(demoServlet());
        // 设置规则
        servletRegistrationBean.addUrlMappings("/cemo/*");
        // 设置加载顺序,即init方法加载顺序(值越小,越先执行,项目启动时加载需要值>0,值如果<0,则请求时才会加载)
        servletRegistrationBean.setLoadOnStartup(1);
        // 返回spring boot listener注册器对象
        return servletRegistrationBean;
    }

}
