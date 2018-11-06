package com.pagehelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class SpringBootStartApplication extends SpringBootServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(SpringBootStartApplication.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {

        for (int i = 0; i < 1; i++) {
            logger.debug("SpringApplicationBuilder.debug...................................."+i);
            logger.info("SpringApplicationBuilder.info...................................."+i);
            logger.warn("SpringApplicationBuilder.warn...................................."+i);
            logger.error("SpringApplicationBuilder.error...................................."+i);
        }

        // 注意这里要指向原先用main方法执行的Application启动类,这样可以读取到Application类中的注解等元信息,至于其中的main方法,则不会进行调用.
        return builder.sources(Application.class);
    }

}
