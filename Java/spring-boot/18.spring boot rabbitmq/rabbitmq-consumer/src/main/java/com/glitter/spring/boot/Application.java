package com.glitter.spring.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.glitter.spring.boot")
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args){
        /*
		连接RabbitMQ时用到的用户名和密码，是RabbitConfig.java中通过@Value注解来实现的，
		对应的值来自启动docker时传入的环境变量mq.rabbit.username和mq.rabbit.password，
		在启动容器后，连接RabbitMQ经常报错提示：ACCESS_REFUSED - Login was refused using authen，
		这里怀疑是springboot应用启动后，从环境变量中取得的mq.rabbit.username和mq.rabbit.password有问题，
		目前的猜测是容器内的环境变量还没有被docker设置完毕，springboot应用就启动起来了，
		因此，在此加一个延时，晚一点启动应用，等待docker将环境变量设置完毕
		*/
        try{
            Thread.sleep(12000);
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("main方法开始");
        SpringApplication.run(Application.class, args);
        System.out.println("main方法结束");
    }

}
