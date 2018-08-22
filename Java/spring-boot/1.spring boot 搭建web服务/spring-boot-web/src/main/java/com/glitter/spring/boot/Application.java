package com.glitter.spring.boot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args){
        System.out.print("进入main方法");
        SpringApplication.run(Application.class, args);
        System.out.print("执行完毕main方法");
    }
}
