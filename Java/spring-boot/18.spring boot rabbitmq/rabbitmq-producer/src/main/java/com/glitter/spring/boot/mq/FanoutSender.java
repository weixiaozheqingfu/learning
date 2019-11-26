package com.glitter.spring.boot.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class FanoutSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.rabbit.exchange.name}")
    String exchangeName;

    public String send(String name, String message) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = simpleDateFormat.format(new Date());
        String sendMessage = "hello, " + name + ", " + message  + ", " + timeStr;

        rabbitTemplate.convertAndSend(exchangeName,"", sendMessage);

        return "send message to [" +  name + "] success (" + timeStr + ")";
    }

}
