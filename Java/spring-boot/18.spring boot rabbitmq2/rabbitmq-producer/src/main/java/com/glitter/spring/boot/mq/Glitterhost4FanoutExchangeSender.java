package com.glitter.spring.boot.mq;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Glitterhost4FanoutExchangeSender {

    private static final Logger logger = LoggerFactory.getLogger(Glitterhost4FanoutExchangeSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value(RabbitConfig.GLITTERHOST_SECOND_FANOUT_EXCHANGE)
    String exchange;

    public String send(String message) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        // rabbitTemplate如果为单例的话，那回调就是最后设置的内容
        long threadName = Thread.currentThread().getId();

        rabbitTemplate.convertAndSend(exchange,null, message, correlationData);
        logger.info("GlitterhostSecondFanoutExchangeSender rabbitTemplate:" + rabbitTemplate.toString());

        // 官方最新文档,从版本2.1开始,该CorrelationData对象具有ListenableFuture您可以用来获取结果的对象,而不是ReturnCallback在模板上使用
        Message returnedMessage = correlationData.getReturnedMessage();

        long threadName2 = Thread.currentThread().getId();

        if (returnedMessage == null) {
            logger.info("GlitterhostSecondFanoutExchangeSender returnedMessage:" + null);
        } else {
            System.out.println("GlitterhostSecondFanoutExchangeSender returnedMessage:" + JSONObject.toJSONString(returnedMessage));
            System.out.println("GlitterhostSecondFanoutExchangeSender returnedMessage exchange:" + returnedMessage.getMessageProperties().getReceivedExchange());
            System.out.println("GlitterhostSecondFanoutExchangeSender returnedMessage routingKey:" + returnedMessage.getMessageProperties().getReceivedRoutingKey());
            return "error";
        }

        long threadName3 = Thread.currentThread().getId();

        // 官方最新文档,从版本2.1开始,该CorrelationData对象具有ListenableFuture您可以用来获取结果的对象,而不是ConfirmCallback在模板上使用
        boolean ack = false;
        try {
            ack = correlationData.getFuture().get().isAck();
            String cause = correlationData.getFuture().get().getReason();
            // 如果ACK true 进行业务处理
            if(ack){
                logger.info("GlitterhostSecondFanoutExchangeSender correlationData:{},ack:{},发送消息成功", correlationData.getId(), ack);
            }else{
                logger.info("GlitterhostSecondFanoutExchangeSender correlationData:{},ack:{},发送消息失败,cause:{}", correlationData.getId(), ack, cause);
                return "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return correlationData.getId();
    }

}
