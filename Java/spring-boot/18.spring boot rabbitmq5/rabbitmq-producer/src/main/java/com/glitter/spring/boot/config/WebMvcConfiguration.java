package com.glitter.spring.boot.config;

import com.glitter.spring.boot.web.interceptor.LoginInterceptor;
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

    @Bean
    LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> requestInterceptorAddPathPatterns = new ArrayList<>();
        requestInterceptorAddPathPatterns.add("/**");

        List<String> loginInterceptorAddPathPatterns = new ArrayList<>();
        loginInterceptorAddPathPatterns.add("/userInfo/*");
        loginInterceptorAddPathPatterns.add("/login/logout");

        registry.addInterceptor(requestInterceptor()).addPathPatterns(requestInterceptorAddPathPatterns);
        registry.addInterceptor(loginInterceptor()).addPathPatterns(loginInterceptorAddPathPatterns);
    }
}
