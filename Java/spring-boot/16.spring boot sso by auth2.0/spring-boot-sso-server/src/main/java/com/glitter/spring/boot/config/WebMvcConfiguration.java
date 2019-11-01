package com.glitter.spring.boot.config;

import com.glitter.spring.boot.web.interceptor.JsessionidByCookieInterceptor;
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
    JsessionidByCookieInterceptor jsessionidByCookieInterceptor(){
        return new JsessionidByCookieInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> requestInterceptorAddPathPatterns = new ArrayList<>();
        requestInterceptorAddPathPatterns.add("/**");
        registry.addInterceptor(requestInterceptor()).addPathPatterns(requestInterceptorAddPathPatterns);

        List<String> jsessionidByCookieInterceptorAddPathPatterns = new ArrayList<>();
        jsessionidByCookieInterceptorAddPathPatterns.add("/oauth2/authorize");
        jsessionidByCookieInterceptorAddPathPatterns.add("/oauth2/login");
        registry.addInterceptor(jsessionidByCookieInterceptor()).addPathPatterns(jsessionidByCookieInterceptorAddPathPatterns);
    }
}
