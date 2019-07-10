package com.glitter.spring.boot.config;

import com.glitter.spring.boot.web.interceptor.DemoInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Bean
    DemoInterceptor demoInterceptor(){
        return new DemoInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 推荐使用这种方式
        List<String> demoInterceptorAddPathPatterns = new ArrayList<>();
        demoInterceptorAddPathPatterns.add("/**");
        demoInterceptorAddPathPatterns.add("/login/**");
        List<String> demoInterceptorExcludePathPatterns = new ArrayList<>();
        demoInterceptorExcludePathPatterns.add("/aemo/**");

        registry.addInterceptor(demoInterceptor()).addPathPatterns(demoInterceptorAddPathPatterns).excludePathPatterns(demoInterceptorExcludePathPatterns);
    }

}
