package com.glitter.spring.boot.mq;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.mq.PublishCallBackInfo;
import com.glitter.spring.boot.config.RabbitConfig;
import com.glitter.spring.boot.context.RabbitmqContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

// 注意生产者的消息队列确认机制与该消息是否消费成功无关
// 生产者只关心消息是否正确找到了队列并正确存储消息到队列
// 建议还是用本类中的写法,比较清晰,不要使用最新的那种写到一块的写法
@Component
public class Glitterhost3FanoutExchangeSender implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {

    private static final Logger logger = LoggerFactory.getLogger(Glitterhost3FanoutExchangeSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value(RabbitConfig.GLITTERHOST_FIRST_FANOUT_EXCHANGE)
    String exchange;

    public String send(String message) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());

        // rabbitTemplate如果为单例的话，那回调就是最后设置的内容
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);

        // 当mandatory参数设为true时,交换器无法根据自身的类型和路由键找到一个符合条件的队列,那么RabbitMQ会调用Basic.Return命令将消息返回给生产者
        // 当mandatory参数设置为false时,出现上述情形,则消息直接被丢弃
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.convertAndSend(exchange,null, message,correlationId);
        logger.info("GlitterhostThirdFanoutExchangeSender rabbitTemplate:" + rabbitTemplate.toString());

        long threadName = Thread.currentThread().getId();
        return correlationId.toString();
    }

    // 消息不能按照指定投递规则正常路由到应该路由到的队列时,消息会返回(前提设置了mandatory参数值为true)
    // 如exchange不存在,queue不存在,指定路由key路由不到,即最终消息未找到投递的队列。
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        long threadName = Thread.currentThread().getId();

        // 如果收到ack反馈,并且ack反馈是false,则此处不再进行处理。
        if (null != RabbitmqContext.get() && !RabbitmqContext.get().isAck()) {
            return;
        } else {
            PublishCallBackInfo publishCallBackInfo = new PublishCallBackInfo();
            publishCallBackInfo.setCorrelationId(message.getMessageProperties().getCorrelationId());
            publishCallBackInfo.setReturnedError(false);
            RabbitmqContext.set(publishCallBackInfo);

            System.out.println("returnedMessage message:" + JSONObject.toJSONString(message));
            System.out.println("returnedMessage replyCode:" + replyCode);
            System.out.println("returnedMessage replyText:" + replyText);
            System.out.println("returnedMessage exchange:" + exchange);
            System.out.println("returnedMessage routingKey:" + routingKey);
        }
    }

    // 1.ack:true:消息正常投递到所有应该投递的队列中去(按exchange投递策略),并且在队列持久化成功(如果设置了消息或队列的持久化)
    // 2.ack:false;即nack:如磁盘写满;MQ出现异常;queue容量到达上限等情况。
    // 3.使用了spring框架后,不支持接收来自mq的批量确认消息了,只能接收来自mq的单条确认消息。
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        long threadName = Thread.currentThread().getId();

        // 如果收到returned反馈,并且returned反馈是false,则此处不再进行处理。
        if (null != RabbitmqContext.get() && !RabbitmqContext.get().isReturnedError()) {
            return;
        } else {
            if (ack) {
                logger.info("GlitterhostFirstFanoutExchangeSender correlationData:{},ack:{},发送消息成功", correlationData.getId(), ack);
            } else {
                logger.info("GlitterhostFirstFanoutExchangeSender correlationData:{},ack:{},发送消息失败,cause:{}", correlationData.getId(), ack, cause);
                PublishCallBackInfo publishCallBackInfo = new PublishCallBackInfo();
                publishCallBackInfo.setCorrelationId(correlationData.getId());
                publishCallBackInfo.setAck(ack);
                RabbitmqContext.set(publishCallBackInfo);
            }
        }
    }


}
