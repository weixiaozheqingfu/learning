package com.glitter.spring.boot.mq;

import com.glitter.spring.boot.config.RabbitmqGlitterhostConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitmqGlitterhostConfig.GLITTERHOST_FIRST_FANOUT_QUEUE, containerFactory = RabbitmqGlitterhostConfig.GLITTERHOST_CONTAINER_FACTORY)
public class GlitterhostFirstFanoutQueueReceiver {
    private static final Logger logger = LoggerFactory.getLogger(GlitterhostFirstFanoutQueueReceiver.class);

    @RabbitHandler
    public void process(String message) {
        logger.info("GlitterhostFirstFanoutQueueReceiver receive message : " + message);
    }

}
