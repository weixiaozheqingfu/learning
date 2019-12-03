package com.glitter.spring.boot.mq;

import com.glitter.spring.boot.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitConfig.GLITTERHOST_SECOND_FANOUT_QUEUE, containerFactory = RabbitConfig.GLITTERHOST_CONTAINER_FACTORY)
public class GlitterhostSecondFanoutQueueReceiver {
    private static final Logger logger = LoggerFactory.getLogger(GlitterhostSecondFanoutQueueReceiver.class);

    @RabbitHandler
    public void process(String message) {
        logger.info("GlitterhostSecondFanoutQueueReceiver receive message : " + message);
    }

}
