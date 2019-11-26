package com.glitter.spring.boot.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Description : fanout消息的接收类
 * @Author : zq2599@gmail.com
 * @Date : 2018-05-06 18:02
 */
@Component
@RabbitListener(queues = "${mq.rabbit.queue.name}")
public class FanoutReceiver {
    private static final Logger logger = LoggerFactory.getLogger(FanoutReceiver.class);

    @RabbitHandler
    public void process(String message) {
        logger.info("receive message : " + message);
    }
}
