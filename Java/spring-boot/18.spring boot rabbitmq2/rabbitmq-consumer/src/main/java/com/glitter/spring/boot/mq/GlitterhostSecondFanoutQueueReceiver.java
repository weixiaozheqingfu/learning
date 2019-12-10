package com.glitter.spring.boot.mq;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.stereotype.Component;

@Component
public class GlitterhostSecondFanoutQueueReceiver {
    private static final Logger logger = LoggerFactory.getLogger(GlitterhostSecondFanoutQueueReceiver.class);

    @RabbitListener(queues = RabbitConfig.GLITTERHOST_SECOND_FANOUT_QUEUE, containerFactory = RabbitConfig.GLITTERHOST_CONTAINER_FACTORY)
    public void process(Message message, Channel channel) throws Exception {
        // 通过basic.qos方法设置prefetch_count=1,这样RabbitMQ就会使得每个Consumer在同一个时间点最多处理一个Message,
        // 换句话说,在接收到该Consumer的ack前,它不会将新的Message分发给它
        channel.basicQos(1);
        long deliveryTag = (Long)message.getMessageProperties().getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        String correlationId = message.getMessageProperties().getHeaders().get("spring_returned_message_correlation").toString();
        try {
            byte[] body = message.getBody();
            String messageStr = new String(body);

            logger.info("GlitterhostSecondFanoutQueueReceiver receive message:{}, messageStr:{}", JSONObject.toJSONString(message), messageStr);
            channel.basicAck(deliveryTag, false);
            JSONObject jsonObject = JSONObject.parseObject(messageStr);

            // 手工确认消费成功,单条确认,确认当前deliveryTag这一条消息
            channel.basicAck(deliveryTag, false);

            // 手工确认消费成功,批量确认,确认deliveryTag消息和其之前的所有消息
            channel.basicAck(deliveryTag, true);
        } catch (Exception e){
            // 手工确认消费失败,单条确认,确认当前deliveryTag这一条消息,消息重新排队
            channel.basicNack(deliveryTag, false, true);

            // 手工确认消费失败,单条确认,确认当前deliveryTag这一条消息,消息丢弃或放入死信队列(前提是队列配置了死信交换器并做了相关设置)
            channel.basicNack(deliveryTag, false, false);

            // 手工确认消费失败,批量确认,确认deliveryTag消息和其之前的所有消息,消息重新排队
            channel.basicNack(deliveryTag, true, true);

            // 手工确认消费失败,批量确认,确认deliveryTag消息和其之前的所有消息,消息丢弃或放入死信队列(前提是队列配置了死信交换器并做了相关设置)
            channel.basicNack(deliveryTag, true, true);

            // 手工确认消费失败,单条确认,确认当前deliveryTag这一条消息,消息重新排队
            channel.basicReject(deliveryTag, true);

            // 手工确认消费失败,单条确认,确认当前deliveryTag这一条消息,消息丢弃或放入死信队列(前提是队列配置了死信交换器并做了相关设置)
            channel.basicReject(deliveryTag, false);

            // 手工确认消费失败,单条确认,确认当前deliveryTag这一条消息,消息丢弃或放入死信队列(前提是队列配置了死信交换器并做了相关设置)
            channel.basicReject(deliveryTag, false);
            logger.error("GlitterhostSecondFanoutQueueReceiver receive message exception" + JSONObject.toJSONString(e));
        }
    }






}
