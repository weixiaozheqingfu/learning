package com.glitter.spring.boot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.glitter.spring.boot"})
/** 说明:使用spring boot 内置tomcat容器需要配置该注解,使用外置tomcat容器不需要该注解 */
@ServletComponentScan
public class Application {

    public static void main(String[] args){
        System.out.println("main方法开始");
        SpringApplication.run(Application.class, args);
        System.out.println("main方法结束");
    }

}
