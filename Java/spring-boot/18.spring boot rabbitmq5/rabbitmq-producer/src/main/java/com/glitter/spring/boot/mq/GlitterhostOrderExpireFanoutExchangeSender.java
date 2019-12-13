package com.glitter.spring.boot.mq;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.mq.OrderInfo;
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
public class GlitterhostOrderExpireFanoutExchangeSender implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {

    private static final Logger logger = LoggerFactory.getLogger(GlitterhostOrderExpireFanoutExchangeSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value(RabbitConfig.GLITTERHOST_ORDER_EXPIRE_FANOUT_EXCHANGE)
    String exchange;

    public String send(OrderInfo orderInfo) {
        try {
            rabbitTemplate.setConfirmCallback(this);
            rabbitTemplate.setReturnCallback(this);

            // 构建correlationData信息
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

            // 构建messageProperties信息
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setCorrelationId(correlationData.getId());

            // 构建message
            logger.info("系统当前时间:{}",System.currentTimeMillis());
            Message message = rabbitTemplate.getMessageConverter().toMessage(orderInfo, messageProperties);

            logger.info("GlitterhostOrderExpireFanoutExchangeSender,message:{},correlationData:{}", JSONObject.toJSON(message), JSONObject.toJSON(correlationData));

            rabbitTemplate.convertAndSend(exchange,null, message, correlationData);

            return correlationData.toString();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("returnedMessage message:" + JSONObject.toJSONString(message));
        System.out.println("returnedMessage replyCode:" + replyCode);
        System.out.println("returnedMessage replyText:" + replyText);
        System.out.println("returnedMessage exchange:" + exchange);
        System.out.println("returnedMessage routingKey:" + routingKey);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            logger.info("GlitterhostFirstFanoutExchangeSender correlationData:{},ack:{},发送消息成功", correlationData.getId(), ack);
        } else {
            logger.info("GlitterhostFirstFanoutExchangeSender correlationData:{},ack:{},发送消息失败,cause:{}", correlationData.getId(), ack, cause);
        }
    }

}
