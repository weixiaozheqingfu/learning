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
public class GlitterhostFirstFanoutQueueReceiver {
    private static final Logger logger = LoggerFactory.getLogger(GlitterhostFirstFanoutQueueReceiver.class);

//    @RabbitHandler
//    public void process(String message) {
//        logger.info("GlitterhostFirstFanoutQueueReceiver receive message : " + message);
//    }

    // 1.建议就逐条确认,因为消息本身就是一条一条来的.
    // 2.建议如果消费失败,就将信息导入死信队列,而不是重新排队,因为既然第一次不能被正常消费,一般是因为业务问题,再次排队,一般情况下还会是失败.
    // 3.如果有批量处理的需要,那就设置channel.basicQos(10)，这样每次客户端可以收到10条数据，由于spring监听方法也是做了一层封装，这10条消息会遍历当前监听方法拿到，
    //   这10条消息反正是一次性都推到客户端了，客户端只要遍历这10条消息就可以了,遍历过程中,可以一条一条确认,也可以憋着不确认,等验证什么业务条件通过时,一并确认。
    @RabbitListener(queues = RabbitConfig.GLITTERHOST_FIRST_FANOUT_QUEUE, containerFactory = RabbitConfig.GLITTERHOST_CONTAINER_FACTORY)
    public void process(Message message, Channel channel) throws Exception {
        long threadId = Thread.currentThread().getId();
        logger.info("threadId:{}",threadId);
        // 建议这里可以不用设置了,对于相同的信道这里会覆盖工厂中的设置,但是这也会造成相同信道第一次和后续每次推送的消息数不一致。
        // 再说了,这个值在工厂处全局统一了,这里就没必要画蛇添足了。
        channel.basicQos(2);

        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        boolean redelivered = message.getMessageProperties().getRedelivered();
        String correlationId = message.getMessageProperties().getCorrelationId();
        String correlationId2 = message.getMessageProperties().getHeaders().get("spring_returned_message_correlation").toString();
        try {
            byte[] body = message.getBody();
            String messageStr = new String(body);

            logger.info("GlitterhostSecondFanoutQueueReceiver receive message:{}, messageStr:{}", JSONObject.toJSONString(message), messageStr);
            JSONObject jsonObject = JSONObject.parseObject(messageStr);

            // 手工确认消费成功,单条确认,确认当前deliveryTag这一条消息
            channel.basicAck(deliveryTag, false);
        } catch (Exception e){
            // 如果是重新放入队头的消息,仍然消费失败,则抛弃该消息,每条消息只给一次重试的机会,重试失败,则抛弃。最好是进入死信队列。
            // 但是一般情况下,第一次不成功,重试成功的概率也不大,不如直接抛弃进入死信队列。
            // 另外,多个消费者客户端消费同一个队列,如果消息重新放入队列中,该信息重新排在队头,理论上每个客户端都可以重新消费这条消息,事实也是如此.
            // 但是经过实验当前客户端重新消费到该消息的可能性更大,队列中的消息是轮询客户端推送消费消息的,则可能会陆续分配消费队列后面的消息给其他客户端,
            // 那么重新归队的这条消息可能反而被后消费(尤其是重试次数多de话,效果就非常明显了)。
            // 可以想象一下这个场景,这条消息不断的重试被排在队头,其他消息陆续被消费掉,当前这条消息可能在重试n次的时候被正确消费掉了,它就慢了。
            // 是否重试,我觉得主要看消息是否考虑有序性,如果无序,可以重试,如果有序,就要从长计议了。
            if (redelivered) {
                // 手工确认消费失败,单条确认,确认当前deliveryTag这一条消息,消息丢弃或放入死信队列(前提是队列配置了死信交换器并做了相关设置)
                channel.basicReject(deliveryTag, false);
            } else {
                // 手工确认消费失败,单条确认,确认当前deliveryTag这一条消息,消息重新放置在消息队列对头,该消息可能被当前客户端和其他客户端再次消费到
                channel.basicReject(deliveryTag, true);
            }
            logger.error("GlitterhostSecondFanoutQueueReceiver receive message exception" + JSONObject.toJSONString(e));
        }
    }

}
