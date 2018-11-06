package com.pagehelper.config;

import com.pagehelper.plugin.PagePlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPagePluginConfiguration {

    @Bean
    public PagePlugin pagePlugin(){
        return new PagePlugin("mysql");
    }
 
}