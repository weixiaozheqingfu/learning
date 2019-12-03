package com.glitter.spring.boot.mq;

import com.glitter.spring.boot.config.GlitterhostRabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = GlitterhostRabbitConfig.GLITTERHOST_SECOND_FANOUT_QUEUE, containerFactory = GlitterhostRabbitConfig.GLITTERHOST_CONTAINER_FACTORY)
public class GlitterhostSecondFanoutQueueReceiver {
    private static final Logger logger = LoggerFactory.getLogger(GlitterhostSecondFanoutQueueReceiver.class);

    @RabbitHandler
    public void process(String message) {
        logger.info("GlitterhostSecondFanoutQueueReceiver receive message : " + message);
    }

}
