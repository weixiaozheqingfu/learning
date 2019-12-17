package com.glitter.spring.boot.mq;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.aop.log.bean.mq.OrderInfo;
import com.glitter.spring.boot.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GlitterOrderExpireDlxDirectQuqueReceiver {
    private static final Logger logger = LoggerFactory.getLogger(GlitterOrderExpireDlxDirectQuqueReceiver.class);

    @RabbitListener(queues = RabbitConfig.GLITTERHOST_ORDER_EXPIRE_DLX_DIRECT_QUEUE, containerFactory = RabbitConfig.GLITTERHOST_CONTAINER_FACTORY)
    public void handleMessage(@Payload OrderInfo orderInfo, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        logger.info("系统当前时间:{}",System.currentTimeMillis());
        logger.info("orderInfo:{},headers:{}", JSONObject.toJSONString(orderInfo), JSONObject.toJSONString(headers));

        long deliveryTag = 0;
        boolean redelivered = false;
        String correlationId = null;
        String zidingyi = null;
        try {
            deliveryTag = Long.valueOf(String.valueOf(headers.get("amqp_deliveryTag")));
            redelivered = Boolean.valueOf(String.valueOf(headers.get("amqp_redelivered")));
            correlationId = String.valueOf(headers.get("amqp_correlationId"));
            zidingyi = String.valueOf(headers.get("zidingyi"));

            long orderId = orderInfo.getId();
            // TODO 业务处理,取消订单
            // int i = 1/0;

            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            logger.error("GlitterOrderExpireDlxDirectQuqueReceiver receive message exception:{}", JSONObject.toJSONString(e));
            channel.basicReject(deliveryTag, true);
        }
    }

}
