package com.glitter.spring.boot.config;

import com.glitter.spring.boot.web.interceptor.CorsInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Bean
    CorsInterceptor corsInterceptor(){
        return new CorsInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> corsInterceptorAddPathPatterns = new ArrayList<>();
        corsInterceptorAddPathPatterns.add("/**");
        registry.addInterceptor(corsInterceptor()).addPathPatterns(corsInterceptorAddPathPatterns);
    }

}
