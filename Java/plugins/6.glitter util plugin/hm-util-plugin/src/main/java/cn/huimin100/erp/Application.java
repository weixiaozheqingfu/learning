package cn.huimin100.erp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args){
        System.out.println("main方法开始");
        SpringApplication.run(Application.class, args);
        for (int i = 0; i < 1; i++) {
            logger.trace("Application.trace...................................."+i);
            logger.debug("Application.debug...................................."+i);
            logger.info("Application.info...................................."+i);
            logger.warn("Application.warn...................................."+i);
            logger.error("Application.error...................................."+i);
        }
        System.out.println("main方法结束");
    }

}
