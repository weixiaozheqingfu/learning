package com.glitter.spring.boot.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class RoothostFirstFanoutExchangeSender {

    private static final Logger logger = LoggerFactory.getLogger(RoothostFirstFanoutExchangeSender.class);

    @Resource(name = "RabbitRoothostTemplate")
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.rabbitmq.roothost.exchange.name}")
    String exchangeName;

    public String send(String name, String message) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = simpleDateFormat.format(new Date());
        String sendMessage = "hello, " + name + ", " + message  + ", " + timeStr;

        rabbitTemplate.convertAndSend(exchangeName,"", sendMessage);
        logger.info("RoothostFirstFanoutExchangeSender rabbitTemplate:" + rabbitTemplate.toString());

        return "RoothostFirstFanoutExchangeSender send message to [" +  name + "] success (" + timeStr + ")";
    }

}
