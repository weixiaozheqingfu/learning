package com.glitter.spring.boot.config;

import com.glitter.spring.boot.observer.CommonPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfiguration {

    @Bean
    public CommonPublisher glitterPublisher() {
        return new CommonPublisher();
    }

}
