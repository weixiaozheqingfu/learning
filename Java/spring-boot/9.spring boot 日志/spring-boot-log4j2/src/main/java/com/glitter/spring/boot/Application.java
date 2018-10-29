package com.glitter.spring.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.glitter.spring.boot"})
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args){
        System.out.println("main方法开始");
        SpringApplication.run(Application.class, args);
        System.out.println("main方法结束");

        for (int i = 0; i < 10000000; i++) {
            logger.debug("Application.debug...................................."+i);
            logger.info("Application.info...................................."+i);
            logger.warn("Application.warn...................................."+i);
            logger.error("Application.error...................................."+i);
        }
        
    }

}
