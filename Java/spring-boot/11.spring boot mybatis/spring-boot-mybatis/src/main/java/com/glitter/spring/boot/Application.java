package com.glitter.spring.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.glitter.spring.boot"})
@MapperScan({"com.glitter.spring.boot.dao"})
public class  Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args){
        System.out.println("main方法开始");
        SpringApplication.run(Application.class, args);
        System.out.println("main方法结束");

        Long begin = System.currentTimeMillis();
        logger.info("开始时间:" + begin);
        for (int i = 0; i < 1; i++) {
            logger.debug("Application.debug...................................."+i);
            logger.info("Application.info...................................."+i);
            logger.warn("Application.warn...................................."+i);
            logger.error("Application.error...................................."+i);
        }
        Long end = System.currentTimeMillis();
        logger.info("结束时间:" + end);
        logger.info("耗时:" + (end - begin) + "毫秒");
    }

}
