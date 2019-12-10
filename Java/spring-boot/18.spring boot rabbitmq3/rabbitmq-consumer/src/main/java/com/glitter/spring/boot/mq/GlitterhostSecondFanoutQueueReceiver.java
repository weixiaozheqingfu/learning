package com.glitter.spring.boot.mq;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class GlitterhostSecondFanoutQueueReceiver {
    private static final Logger logger = LoggerFactory.getLogger(GlitterhostSecondFanoutQueueReceiver.class);

    @RabbitListener(queues = RabbitConfig.GLITTERHOST_SECOND_FANOUT_QUEUE, containerFactory = RabbitConfig.GLITTERHOST_CONTAINER_FACTORY)
    public void process(Message message, Channel channel) throws Exception {
        long threadId = Thread.currentThread().getId();
        logger.info("threadId:{}",threadId);

        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        boolean redelivered = message.getMessageProperties().getRedelivered();
        String correlationId = message.getMessageProperties().getCorrelationId();

        try {
            byte[] body = message.getBody();
            String messageStr = new String(body);

            logger.info("GlitterhostSecondFanoutQueueReceiver receive message:{}, messageStr:{}", JSONObject.toJSONString(message), messageStr);
            JSONObject jsonObject = JSONObject.parseObject(messageStr);

            channel.basicAck(deliveryTag, false);
        } catch (Exception e){
            if (redelivered) {
                channel.basicReject(deliveryTag, false);
            } else {
                channel.basicReject(deliveryTag, true);
            }
            logger.error("GlitterhostSecondFanoutQueueReceiver receive message exception" + JSONObject.toJSONString(e));
        }
    }

}
