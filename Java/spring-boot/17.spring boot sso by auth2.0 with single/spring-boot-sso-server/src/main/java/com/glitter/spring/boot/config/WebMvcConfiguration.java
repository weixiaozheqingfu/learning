package com.glitter.spring.boot.config;

import com.glitter.spring.boot.web.interceptor.JsessionidByCookieInterceptor;
import com.glitter.spring.boot.web.interceptor.OauthResourceInterceptor;
import com.glitter.spring.boot.web.interceptor.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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

    @Bean
    OauthResourceInterceptor oauthResourceInterceptor(){
        return new OauthResourceInterceptor();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//              .allowedOrigins("http://*.sso.sso.glitter.com")
                .allowedOrigins("*")
                .allowedMethods("PUT", "DELETE", "POST", "GET", "OPTIONS")
                .allowedHeaders("x-requested-with", "content-type", "accept", "origin")
                .allowCredentials(true).maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> requestInterceptorAddPathPatterns = new ArrayList<>();
        requestInterceptorAddPathPatterns.add("/**");
        registry.addInterceptor(requestInterceptor()).addPathPatterns(requestInterceptorAddPathPatterns);

        List<String> jsessionidByCookieInterceptorAddPathPatterns = new ArrayList<>();
        jsessionidByCookieInterceptorAddPathPatterns.add("/sso/authorize");
        jsessionidByCookieInterceptorAddPathPatterns.add("/sso/login");
        registry.addInterceptor(jsessionidByCookieInterceptor()).addPathPatterns(jsessionidByCookieInterceptorAddPathPatterns);

        List<String> oauthResourceInterceptorAddPathPatterns = new ArrayList<>();
        oauthResourceInterceptorAddPathPatterns.add("/sso/resource/userinfo");
        oauthResourceInterceptorAddPathPatterns.add("/sso/resource/keepAlive");
        oauthResourceInterceptorAddPathPatterns.add("/sso/resource/logout");
        registry.addInterceptor(oauthResourceInterceptor()).addPathPatterns(oauthResourceInterceptorAddPathPatterns);
    }
}
