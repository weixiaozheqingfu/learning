package com.glitter.spring.boot.config;

import com.glitter.spring.boot.web.interceptor.AemoInterceptor;
import com.glitter.spring.boot.web.interceptor.DemoInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 强调一点：只有经过DispatcherServlet 的请求，才会走拦截器链，我们自定义的Servlet 请求是不会被拦截的，
 * 比如我们自定义的Servlet地址 http://localhost:8080/xs/myservlet 是不会被拦截器拦截的。
 * 道理很简单,拦截器是spring的,只有经过spring的DispatcherServlet入口进来的请求才会执行后续的拦截器。
 * 自定义的servle与DispatcherServlet属于平级的servelt,按照servlet api请求后直接返回结果了,当然不会走spring的拦截器。
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Bean
    AemoInterceptor aemoInterceptor(){
        return new AemoInterceptor();
    }

    @Bean
    DemoInterceptor demoInterceptor(){
        return new DemoInterceptor();
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(aemoInterceptor()).addPathPatterns("/aemo/**");
//        registry.addInterceptor(demoInterceptor()).addPathPatterns("/demo/**");
//    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
          // 注意排除的写法不能单独追加,需要按照此写法后续追加才有效.
//        registry.addInterceptor(aemoInterceptor()).addPathPatterns("/**").excludePathPatterns("/demo/**");
//        registry.addInterceptor(demoInterceptor()).addPathPatterns("/**").excludePathPatterns("/aemo/**");
//    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 如果请求命中多个拦截器,则多个拦截器的执行顺序与此处代码的先后顺序有关系,具体执行顺序如下
//        // 假定interceptor1就是aemoInterceptor,interceptor2就是demoInterceptor,此处代码interceptor1在interceptor2之前.
//        // =======> interceptor1:preHandle()
//        // =======> interceptor2:preHandle()
//        // =======> interceptor2:postHandle()
//        // =======> interceptor1:postHandle()
//        // =======> interceptor2:afterCompletion()
//        // =======> interceptor1:afterCompletion()
//
//        // =======> DemoInterceptor preHandle...............................................
//        // =======> AemoInterceptor preHandle...............................................
//        // =======> AemoInterceptor afterCompletion...............................................
//        // =======> DemoInterceptor afterCompletion...............................................
//        registry.addInterceptor(aemoInterceptor()).addPathPatterns("/**");
//        registry.addInterceptor(demoInterceptor()).addPathPatterns("/**");
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 推荐使用这种方式
        List<String> aemoInterceptorAddPathPatterns = new ArrayList<>();
        aemoInterceptorAddPathPatterns.add("/**");
        aemoInterceptorAddPathPatterns.add("/login/**");
        List<String> aemoInterceptorExcludePathPatterns = new ArrayList<>();
        aemoInterceptorExcludePathPatterns.add("/demo/**");

        List<String> demoInterceptorAddPathPatterns = new ArrayList<>();
        demoInterceptorAddPathPatterns.add("/**");
        demoInterceptorAddPathPatterns.add("/login/**");
        List<String> demoInterceptorExcludePathPatterns = new ArrayList<>();
        demoInterceptorExcludePathPatterns.add("/aemo/**");

        registry.addInterceptor(aemoInterceptor()).addPathPatterns(aemoInterceptorAddPathPatterns).excludePathPatterns(aemoInterceptorExcludePathPatterns);
        registry.addInterceptor(demoInterceptor()).addPathPatterns(demoInterceptorAddPathPatterns).excludePathPatterns(demoInterceptorExcludePathPatterns);
    }

}
