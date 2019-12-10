package com.glitter.spring.boot.mq;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.mq.PublishCallBackInfo;
import com.glitter.spring.boot.config.RabbitConfig;
import com.glitter.spring.boot.context.RabbitmqContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Glitterhost1FanoutExchangeSender implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {

    private static final Logger logger = LoggerFactory.getLogger(Glitterhost1FanoutExchangeSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value(RabbitConfig.GLITTERHOST_FIRST_FANOUT_EXCHANGE)
    String exchange;

    public String send(String message) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());

        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);

        MessageProperties messageProperties = new MessageProperties();
        logger.info(JSONObject.toJSONString(messageProperties));

        Message message1 = new Message(message.getBytes(), messageProperties);
        message1.getMessageProperties().setCorrelationId(correlationId.getId());
        rabbitTemplate.convertAndSend(exchange,null, message1,correlationId);

        logger.info("GlitterhostFirstFanoutExchangeSender rabbitTemplate:{},correlationId:{}", rabbitTemplate.toString(), correlationId);

        long threadName = Thread.currentThread().getId();
        return correlationId.toString();
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        long threadName = Thread.currentThread().getId();

        System.out.println("returnedMessage message:" + JSONObject.toJSONString(message));
        System.out.println("returnedMessage replyCode:" + replyCode);
        System.out.println("returnedMessage replyText:" + replyText);
        System.out.println("returnedMessage exchange:" + exchange);
        System.out.println("returnedMessage routingKey:" + routingKey);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        long threadName = Thread.currentThread().getId();

        if (ack) {
            logger.info("GlitterhostFirstFanoutExchangeSender correlationData:{},ack:{},发送消息成功", correlationData.getId(), ack);
        } else {
            logger.info("GlitterhostFirstFanoutExchangeSender correlationData:{},ack:{},发送消息失败,cause:{}", correlationData.getId(), ack, cause);
            PublishCallBackInfo publishCallBackInfo = new PublishCallBackInfo();
            publishCallBackInfo.setCorrelationId(correlationData.getId());
            publishCallBackInfo.setAck(ack);
            RabbitmqContext.set(publishCallBackInfo);
        }
    }


}
