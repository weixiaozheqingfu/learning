package com.glitter.spring.boot.mq;

import com.glitter.spring.boot.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class GlitterhostFirstFanoutExchangeSender implements RabbitTemplate.ConfirmCallback {

    private static final Logger logger = LoggerFactory.getLogger(GlitterhostFirstFanoutExchangeSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value(RabbitConfig.GLITTERHOST_FIRST_FANOUT_EXCHANGE)
    String exchange;

    public String send(String name, String message) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());

        // rabbitTemplate如果为单例的话，那回调就是最后设置的内容
        this.rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.convertAndSend(exchange,null, message,correlationId);

        logger.info("GlitterhostFirstFanoutExchangeSender rabbitTemplate:" + rabbitTemplate.toString());
        return correlationId.toString();
    }

    // 回调
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println(" 回调id:" + correlationData);
        if (ack) {
            System.out.println("消息成功消费");
        } else {
            System.out.println("消息消费失败:" + cause);
        }
    }

}
