package com.glitter.spring.boot.mq;

import com.glitter.spring.boot.config.RoothostRabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(containerFactory = RoothostRabbitConfig.ROOTHOST_CONTAINER_FACTORY)
public class RoothostFanoutQueueReceiver {
    private static final Logger logger = LoggerFactory.getLogger(RoothostFanoutQueueReceiver.class);

    @RabbitListener(queues = RoothostRabbitConfig.ROOTHOST_FIRST_FANOUT_QUEUE)
    public void roothostFirstFanoutQueueProcess(String message) {
        logger.info("RoothostFirstFanoutQueueReceiver receive message : " + message);
    }

    @RabbitListener(queues = RoothostRabbitConfig.ROOTHOST_SECOND_FANOUT_QUEUE)
    public void roothostSecondFanoutQueueProcess(String message) {
        logger.info("RoothostSecondFanoutQueueReceiver receive message : " + message);
    }

}
