package com.glitter.spring.boot.mq;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.config.RabbitConfig;
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

    public String send(UserInfo userInfo) {
        try {
            long threadName = Thread.currentThread().getId();

            rabbitTemplate.setConfirmCallback(this);
            rabbitTemplate.setReturnCallback(this);

            // 构建correlationData信息
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

            // 构建messageProperties信息
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setCorrelationId(correlationData.getId());
            messageProperties.setMessageId("123456");
            messageProperties.setAppId("1001");
            messageProperties.setUserId("glitter");
            messageProperties.setHeader("zidingyi", "123自定义abc");

            // 构建message
            Message message = rabbitTemplate.getMessageConverter().toMessage(userInfo, messageProperties);

            logger.info("GlitterhostFirstFanoutExchangeSender rabbitTemplate:{},message:{},correlationData:{}", rabbitTemplate.toString(), JSONObject.toJSON(message), JSONObject.toJSON(correlationData));

            rabbitTemplate.convertAndSend(exchange,null, message, correlationData);

            return correlationData.toString();
        } catch (Exception e) {
            throw e;
        }
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
        }
    }

}
