package com.glitter.spring.boot.mq;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class GlitterhostFirstFanoutQueueReceiver {
    private static final Logger logger = LoggerFactory.getLogger(GlitterhostFirstFanoutQueueReceiver.class);

    @RabbitListener(queues = RabbitConfig.GLITTERHOST_FIRST_FANOUT_QUEUE, containerFactory = RabbitConfig.GLITTERHOST_CONTAINER_FACTORY)
    public void handleMessage(@Payload UserInfo userInfo, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        logger.info("userInfo:{},headers:{}", JSONObject.toJSONString(userInfo), JSONObject.toJSONString(headers));
//        for (Map.Entry<String, Object> entry : headers.entrySet()) {
//            logger.info("消息头：" + entry.getKey() + "---" + entry.getValue());
//        }

        long deliveryTag = 0;
        boolean redelivered = false;
        String correlationId = null;
        String zidingyi = null;
        try {
            deliveryTag = Long.valueOf(String.valueOf(headers.get("amqp_deliveryTag")));
            redelivered = Boolean.valueOf(String.valueOf(headers.get("amqp_redelivered")));
            correlationId = String.valueOf(headers.get("amqp_correlationId"));
            zidingyi = String.valueOf(headers.get("zidingyi"));

            logger.info("zidingyi:{}", zidingyi);
            // TODO 业务处理
            int i= 1/0;

            channel.basicAck(deliveryTag, false);
            channel.basicQos(2);
        } catch (Exception e) {
            logger.error("GlitterhostFirstFanoutQueueReceiver receive message exception:{}", JSONObject.toJSONString(e));
            if (redelivered) {
                channel.basicReject(deliveryTag, false);
            } else {
                channel.basicReject(deliveryTag, true);
            }
        }
    }

    /**
     * 死信消息如何处理取决于业务需求,有的死信队列是不需要监听的,如果死信队列有消息,需要手动去处理。
     * 有的信息队列的消息监听,然后将死信队列中的消息读入到数据库中对应的死信记录表中记录。
     *
     * 进入监听方法记录日志日志,同时正常入库,双管齐下保证死信队列的消息被记录下来不丢失。
     *
     * 一般建议mq层面一个队列对应一个死信队列。代码层面,两个监听方法监听正常队列和死信队列。数据库层面要有死信队列记录表,正常队列消费记录表是否有看需要。
     * @param userInfo
     * @param headers
     * @param channel
     * @throws Exception
     */
    @RabbitListener(queues = RabbitConfig.GLITTERHOST_FIRST_DLX_DIRECT_QUEUE, containerFactory = RabbitConfig.GLITTERHOST_CONTAINER_FACTORY)
    public void handleDlxMessage(@Payload UserInfo userInfo, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        logger.info("userInfo:{},headers:{}", JSONObject.toJSONString(userInfo), JSONObject.toJSONString(headers));

        long deliveryTag = 0;
        boolean redelivered = false;
        String correlationId = null;
        String zidingyi = null;
        try {
            deliveryTag = Long.valueOf(String.valueOf(headers.get("amqp_deliveryTag")));
            redelivered = Boolean.valueOf(String.valueOf(headers.get("amqp_redelivered")));
            correlationId = String.valueOf(headers.get("amqp_correlationId"));
            zidingyi = String.valueOf(headers.get("zidingyi"));

            // TODO 业务处理,比如死信消息放入数据库对应的死信记录表中记录。

            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            logger.error("GlitterhostFirstFanoutQueueReceiver receive message exception:{}", JSONObject.toJSONString(e));
            channel.basicReject(deliveryTag, true);
        }
    }

}
