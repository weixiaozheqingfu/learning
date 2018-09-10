package com.glitter.spring.boot.config;

import com.glitter.spring.boot.web.filter.DemoFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class FilterConfiguration implements WebMvcConfigurer {

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
        // 过滤器顺序,值越小,执行顺序越靠前
        filterRegistrationBean.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        // 过滤器初始化参数
        Map initParam = new HashMap<>(1);
        initParam.put("initParam", "1");
        filterRegistrationBean.setInitParameters(initParam);
        // 返回spring boot filter注册器对象
        return filterRegistrationBean;
    }

    @Bean(name = "demoFilter1")
    public DemoFilter demoFilter1() {
        return new DemoFilter();
    }

    @Bean
    public FilterRegistrationBean demoFilter1RegistrationBean() {
        // spring boot filter注册器对象
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        // 注入过滤器
        filterRegistrationBean.setFilter(demoFilter());
        // 过滤规则
        filterRegistrationBean.addUrlPatterns("/*", "/demo/*");
        // 过滤器名称
        filterRegistrationBean.setName("demoFilter1");
        // 过滤器顺序,值越小,执行顺序越靠前
        filterRegistrationBean.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE + 1);
        // 过滤器初始化参数
        Map initParam = new HashMap<>(1);
        initParam.put("initParam", "2");
        filterRegistrationBean.setInitParameters(initParam);
        // 返回spring boot filter注册器对象
        return filterRegistrationBean;
    }

}
