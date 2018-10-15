package com.glitter.spring.boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 外一种解决跨域问题的方法是自定义CorsInterceptor拦截器类自己处理.
 * 推荐使用springboot的这种方式,因为跨域通常这是为了解决开发阶段前后端联调的方便,没必要再单独写一个拦截器.
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins("http://*.systoon.com")
//                .allowedOrigins("http://127.0.0.1")
//                .allowedOrigins("http://localhost")
                  .allowedOrigins("http://localhost:8081")
//                .allowedOrigins("*")
                  .allowedMethods("PUT", "DELETE", "POST", "GET", "OPTIONS")
                  .allowedHeaders("x-requested-with", "content-type", "accept", "origin")
                  .allowCredentials(true).maxAge(3600);
    }

}
