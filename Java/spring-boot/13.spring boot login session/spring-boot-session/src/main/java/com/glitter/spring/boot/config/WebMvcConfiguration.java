package com.glitter.spring.boot.config;

import com.glitter.spring.boot.web.interceptor.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Bean
    RequestInterceptor requestInterceptor(){
        return new RequestInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> aemoInterceptorAddPathPatterns = new ArrayList<>();
        aemoInterceptorAddPathPatterns.add("/**");

        registry.addInterceptor(requestInterceptor()).addPathPatterns(aemoInterceptorAddPathPatterns);
    }
}
