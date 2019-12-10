package com.glitter.spring.boot.mq;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.mq.PublishCallBackInfo;
import com.glitter.spring.boot.config.RabbitConfig;
import com.glitter.spring.boot.context.RabbitmqContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
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
public class Glitterhost1FanoutExchangeSender implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {

    private static final Logger logger = LoggerFactory.getLogger(Glitterhost1FanoutExchangeSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value(RabbitConfig.GLITTERHOST_FIRST_FANOUT_EXCHANGE)
    String exchange;

    public String send(String message) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());

        // rabbitTemplate如果为单例的话，那回调就是最后设置的内容,因为不同的发送消息类的回调处理方法处理逻辑可能会有不同。
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);

        // 发布一条消息时,当指定发布消息相关参数mandatory设为true时,交换器无法根据自身的类型和路由键找到一个符合条件的队列,那么RabbitMQ会调用Basic.Return命令将消息返回给生产者
        // 发布一条消息时,当指定发布消息相关参数mandatory设置为false时,出现上述情形,则消息直接被丢弃
        // 经过实践验证,此处是否设置mandatory对生产者消息确认没有什么作用
        // 这在于spring的处理,如果使用原始mq的api,这个参数一定是有作用的。
        // 对于spring来讲connectionFactory中的设置才是发挥作用的设置。
        rabbitTemplate.setMandatory(false);

        MessageProperties messageProperties = new MessageProperties();
        logger.info(JSONObject.toJSONString(messageProperties));
        Message message1 = new Message(message.getBytes(), messageProperties);
        message1.getMessageProperties().setCorrelationId(correlationId.getId());
        rabbitTemplate.convertAndSend(exchange,null, message1,correlationId);
        logger.info("GlitterhostFirstFanoutExchangeSender rabbitTemplate:{},correlationId:{}", rabbitTemplate.toString(), correlationId);

        long threadName = Thread.currentThread().getId();
        return correlationId.toString();
    }

    // 消息不能按照指定投递规则正常路由到应该路由到的队列时,消息会返回(前提设置了mandatory参数值为true)
    // queue不存在,指定路由key路由不到,即最终消息未找到投递的队列。
    // 比较恶心的一种情况,这两个方法并不是互斥的。路由不到队列会调用returnedMessage,但是居然也会调用confirm方法,并且调用confirm方法时,ack居然时true。
    // 好在returnedMessage和confirm方法是同一个线程,我们可能会想到使用threadLocal标识回调结果。但是不推荐,因为没有合适的时机注销threadLocal,就会造成风险。
    // 虽然下面做了threadLocal的示例,但是并不推荐真的这么去做,并且下面的threadLocal没有找到合适的百分百的remove时机,这样线程回收到线程池后被其他线程拿来重新使用时,还会有之前的数据,那就乱了。
    // 下面的threadLocal仅仅是为了证明他们确实是一个线程。
    // 还是那句话,这两个方法虽然不是互斥的,但如果两个同时被调用,他们还是同一个线程的,那么也可以理解为他们即使都被调用也是一条线上执行的。
    // 那么我们可以考虑消息使用数据库记录,将这两个方法的处理结果使用两个数据库字段去记录,消息是否成功,结合这两个字段的综合来进行分析。只有return为空且ack为true才认为这条消息是投递成功了。
    // 具体实现看后续文章和代码。
    // 其实正常来讲,按照我们的最优方案设计,不可能路由不到对应的队列,并且我们都是经过测试的,所以被return的可能性是比较小的。
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
    // 2.ack:false;即nack:如磁盘写满;MQ出现异常;queue容量到达上限;exchange不存在等情况。
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
