package com.glitter.spring.boot.mq;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class GlitterhostFirstFanoutQueueReceiver {
    private static final Logger logger = LoggerFactory.getLogger(GlitterhostFirstFanoutQueueReceiver.class);

//    @RabbitListener(queues = RabbitConfig.GLITTERHOST_FIRST_FANOUT_QUEUE, containerFactory = RabbitConfig.GLITTERHOST_CONTAINER_FACTORY)
//    public void process(Message message, Channel channel) throws Exception {
//        long threadId = Thread.currentThread().getId();
//        logger.info("threadId:{}",threadId);
//
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        boolean redelivered = message.getMessageProperties().getRedelivered();
//        String correlationId = message.getMessageProperties().getCorrelationId();
//
//        try {
//            byte[] body = message.getBody();
//            String messageStr = new String(body);
//
//            logger.info("GlitterhostSecondFanoutQueueReceiver receive message:{}, messageStr:{}", JSONObject.toJSONString(message), messageStr);
//            JSONObject jsonObject = JSONObject.parseObject(messageStr);
//
//            channel.basicAck(deliveryTag, false);
//        } catch (Exception e){
//            logger.error("GlitterhostSecondFanoutQueueReceiver receive message exception" + JSONObject.toJSONString(e));
//            if (redelivered) {
//                channel.basicReject(deliveryTag, false);
//            } else {
//                channel.basicReject(deliveryTag, true);
//            }
//        }
//    }

    @RabbitListener(queues = RabbitConfig.GLITTERHOST_FIRST_FANOUT_QUEUE, containerFactory = RabbitConfig.GLITTERHOST_CONTAINER_FACTORY)
    public void handleMessage(@Payload UserInfo userInfo, @Headers Map<String, Object> headers, Channel channel) {
        logger.info("userInfo:{}", JSONObject.toJSONString(userInfo));
        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            logger.info("消息头：" + entry.getKey() + "---" + entry.getValue());
        }


        long deliveryTag = Long.valueOf(String.valueOf(headers.get("amqp_deliveryTag")));
        try {
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
