package com.glitter.spring.boot.mq;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitConfig.GLITTERHOST_SECOND_FANOUT_QUEUE, containerFactory = RabbitConfig.GLITTERHOST_CONTAINER_FACTORY)
public class GlitterhostSecondFanoutQueueReceiver {
    private static final Logger logger = LoggerFactory.getLogger(GlitterhostSecondFanoutQueueReceiver.class);

    @RabbitHandler
    public void process(String message) {
        logger.info("GlitterhostSecondFanoutQueueReceiver receive message : " + message);
    }




//    public void onMessage(Message message, Channel channel) throws Exception {
//        byte[] body = message.getBody();
//        logger.info("接收到消息:" + new String(body));
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = JSONObject.parseObject(new String(body));
//            if ("消费成功".equals("消费成功")) {
//                logger.info("消息消费成功");
//
//                //确认消息消费成功
//                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//            } else if ("可重试的失败处理".equals("可重试的失败处理")) {
//                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//                //消费失败
//            } else {
//                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
//            }
//        } catch (Exception e){
//                //消息丢弃
//                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
//                logger.error("This message:" + jsonObject + " conversion JSON error ");
//        }
//
//    }






}
