package com.glitter.spring.boot.config;

import com.glitter.spring.boot.web.filter.AemoFilter;
import com.glitter.spring.boot.web.filter.DemoFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class FilterConfiguration implements WebMvcConfigurer {

    @Bean(name = "aemoFilter")
    public AemoFilter aemoFilter() {
        return new AemoFilter();
    }

    @Bean(name = "demoFilter")
    public DemoFilter demoFilter() {
        return new DemoFilter();
    }

    @Bean
    public FilterRegistrationBean demoFilterRegistrationBean() {
        // spring boot filter注册器对象
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        // 注入过滤器
        filterRegistrationBean.setFilter(demoFilter());
        // 过滤规则
        filterRegistrationBean.addUrlPatterns("/*", "/demo/*");
        // 过滤器名称
        filterRegistrationBean.setName("demoFilter");
        // 过滤器doFilter方法顺序,值越小,执行顺序越靠前。init方法顺序无法控制,与类加载顺序有关,但一般先加载哪个类,就会一直按照这个固定的顺序进行执行的。
        filterRegistrationBean.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        // 过滤器初始化参数
        Map initParam = new HashMap<>();
        initParam.put("initParam", "demoFilter");
        filterRegistrationBean.setInitParameters(initParam);
        // 返回spring boot filter注册器对象
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean aemoFilter1RegistrationBean() {
        // spring boot filter注册器对象
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        // 注入过滤器
        filterRegistrationBean.setFilter(aemoFilter());
        // 过滤规则
        filterRegistrationBean.addUrlPatterns("/*", "/aemo/*");
        // 过滤器名称
        filterRegistrationBean.setName("aemoFilter");
        // 过滤器doFilter方法顺序,值越小,执行顺序越靠前。init方法顺序无法控制,与类加载顺序有关,但一般先加载哪个类,就会一直按照这个固定的顺序进行执行的。
        filterRegistrationBean.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE + 1);
        // 过滤器初始化参数
        Map initParam = new HashMap<>();
        initParam.put("initParam", "aemoFilter");
        filterRegistrationBean.setInitParameters(initParam);
        // 返回spring boot filter注册器对象
        return filterRegistrationBean;
    }
}
