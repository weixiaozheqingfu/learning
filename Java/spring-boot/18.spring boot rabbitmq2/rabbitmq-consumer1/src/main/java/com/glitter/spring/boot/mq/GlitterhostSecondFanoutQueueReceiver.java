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


//    @RabbitHandler
//    public void process(@Payload Book body, @Headers Map<String, Object> headers) {
//        System.out.println("-->信息域的值"+body);
//        for (Map.Entry<String, Object> entry : headers.entrySet()) {
//            System.out.println("消息头：" + entry.getKey() + "---" + entry.getValue());
//        }
//    }

    @RabbitHandler
    public void process(Message message, Channel channel) throws Exception {
        Long deliveryTag = null;
        try {
            byte[] body = message.getBody();
            logger.info("GlitterhostSecondFanoutQueueReceiver receive message : " + new String(body));

            JSONObject jsonObject = JSONObject.parseObject(new String(body));;
            // Long deliveryTag = (Long)message.getMessageProperties().getHeaders().get(AmqpHeaders.DELIVERY_TAG);
            deliveryTag = message.getMessageProperties().getDeliveryTag();

            // 手工确认消费成功,单条确认,确认当前deliveryTag这一条消息
            channel.basicAck(deliveryTag, false);

//            // 手工确认消费成功,批量确认,确认deliveryTag消息和其之前的所有消息
//            channel.basicAck(deliveryTag, true);
//
//            // 手工确认消费失败,单条确认,确认当前deliveryTag这一条消息,消息重新排队
//            channel.basicNack(deliveryTag, false, true);
//
//            // 手工确认消费失败,单条确认,确认当前deliveryTag这一条消息,消息丢弃或放入死信队列(前提是队列配置了死信交换器并做了相关设置)
//            channel.basicNack(deliveryTag, false, false);
//
//            // 手工确认消费失败,批量确认,确认deliveryTag消息和其之前的所有消息,消息重新排队
//            channel.basicNack(deliveryTag, true, true);
//
//            // 手工确认消费失败,批量确认,确认deliveryTag消息和其之前的所有消息,消息丢弃或放入死信队列(前提是队列配置了死信交换器并做了相关设置)
//            channel.basicNack(deliveryTag, true, true);
//
//            // 手工确认消费失败,单条确认,确认当前deliveryTag这一条消息,消息重新排队
//            channel.basicReject(deliveryTag, true);
//
//            // 手工确认消费失败,单条确认,确认当前deliveryTag这一条消息,消息丢弃或放入死信队列(前提是队列配置了死信交换器并做了相关设置)
//            channel.basicReject(deliveryTag, false);
        } catch (Exception e){
            // 手工确认消费失败,单条确认,确认当前deliveryTag这一条消息,消息丢弃或放入死信队列(前提是队列配置了死信交换器并做了相关设置)
            channel.basicReject(deliveryTag, false);
            logger.error("GlitterhostSecondFanoutQueueReceiver receive message exception" + JSONObject.toJSONString(e));
        }
    }






}
