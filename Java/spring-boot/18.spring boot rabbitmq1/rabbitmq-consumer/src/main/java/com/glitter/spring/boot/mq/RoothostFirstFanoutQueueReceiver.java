package com.glitter.spring.boot.mq;

import com.glitter.spring.boot.config.RabbitmqRoothostConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitmqRoothostConfig.ROOTHOST_FIRST_FANOUT_QUEUE, containerFactory = RabbitmqRoothostConfig.ROOTHOST_CONTAINER_FACTORY)
public class RoothostFirstFanoutQueueReceiver {
    private static final Logger logger = LoggerFactory.getLogger(RoothostFirstFanoutQueueReceiver.class);

    @RabbitHandler
    public void process(String message) {
        logger.info("RoothostFirstFanoutQueueReceiver receive message : " + message);
    }
}
