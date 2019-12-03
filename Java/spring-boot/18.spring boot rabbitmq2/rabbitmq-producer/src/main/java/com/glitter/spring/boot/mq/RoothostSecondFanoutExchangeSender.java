package com.glitter.spring.boot.mq;

import com.glitter.spring.boot.config.RoothostRabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class RoothostSecondFanoutExchangeSender {

    private static final Logger logger = LoggerFactory.getLogger(RoothostSecondFanoutExchangeSender.class);

    @Resource(name = "roothostRabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Value(RoothostRabbitConfig.ROOTHOST_SECOND_FANOUT_EXCHANGE)
    String exchange;

    public String send(String name, String message) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = simpleDateFormat.format(new Date());
        String sendMessage = "hello, " + name + ", " + message  + ", " + timeStr;

        rabbitTemplate.convertAndSend(exchange,"", sendMessage);
        logger.info("RoothostSecondFanoutExchangeSender rabbitTemplate:" + rabbitTemplate.toString());

        return "RoothostSecondFanoutExchangeSender send message to [" +  name + "] success (" + timeStr + ")";
    }

}
