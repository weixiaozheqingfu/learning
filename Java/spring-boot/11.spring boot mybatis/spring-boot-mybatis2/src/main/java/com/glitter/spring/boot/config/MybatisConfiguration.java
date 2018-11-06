package com.glitter.spring.boot.config;

import com.glitter.spring.boot.bean.BaseBean;
import com.glitter.spring.boot.plugin.page.PagePlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfiguration extends BaseBean {

    @Bean
    public PagePlugin pagePlugin(){
        return new PagePlugin("mysql");
    }
 
}