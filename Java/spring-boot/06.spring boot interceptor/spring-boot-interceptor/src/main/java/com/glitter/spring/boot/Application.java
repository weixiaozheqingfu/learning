package com.glitter.spring.boot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.glitter.spring.boot"})
public class Application {

    public static void main(String[] args){
        System.out.println("main方法开始");
        SpringApplication.run(Application.class, args);
        System.out.println("main方法结束");
    }

}
