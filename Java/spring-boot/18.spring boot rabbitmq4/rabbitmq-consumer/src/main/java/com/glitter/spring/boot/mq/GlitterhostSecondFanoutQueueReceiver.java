package com.glitter.spring.boot.mq;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.glitter.spring.boot.bean.UserInfo;
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

    // 使用注解接收和使用Message接收方式没有优劣之分,就看你关心什么数据,如果你关心MessageProperties中的固有属性,使用Message接收后获取固有属性信息比较方便。
    // 如果你不关心MessageProperties中的固有属性,那么使用注解接收比较方便。
    // 但是如果使用注解接收,如果出现转换异常,比如生产者不知道什么原因发送了一条消息不是UserInfo类型的,那么spring在调用该方法之前就会报转换异常,那么消息就会一直阻塞。
    // 不过这个问题可以考虑使用消息的超时时间解决,但是一般情况下是不建议使用超时时间的,除非是特定业务或者是延时队列业务需要。普通业务一般不希望消息过期。
    @RabbitListener(queues = RabbitConfig.GLITTERHOST_FIRST_FANOUT_QUEUE, containerFactory = RabbitConfig.GLITTERHOST_CONTAINER_FACTORY)
    public void process(Message message, Channel channel) throws Exception {
        logger.info("userInfo:{},headers:{}", JSONObject.toJSONString(message.getBody()), JSONObject.toJSONString(message.getMessageProperties()));

        long deliveryTag = 0;
        boolean redelivered = false;
        String correlationId = null;
        String zidingyi = null;

        try {
            deliveryTag = message.getMessageProperties().getDeliveryTag();
            redelivered = message.getMessageProperties().getRedelivered();
            correlationId = message.getMessageProperties().getCorrelationId();
            zidingyi = String.valueOf(message.getMessageProperties().getHeaders().get("zidingyi"));

            byte[] body = message.getBody();
            String bodyStr = new String(body);
            UserInfo userInfo = JSONObject.parseObject(bodyStr, new TypeReference<UserInfo>(){});


            // TODO 业务处理

            channel.basicAck(deliveryTag, false);
        } catch (Exception e){
            logger.error("GlitterhostSecondFanoutQueueReceiver receive message exception" + JSONObject.toJSONString(e));
            if (redelivered) {
                channel.basicReject(deliveryTag, false);
            } else {
                channel.basicReject(deliveryTag, true);
            }
        }
    }

}
