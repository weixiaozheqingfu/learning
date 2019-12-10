package com.glitter.spring.boot.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${mq.rabbit.glitterhost.address}")
    String address;
    @Value("${mq.rabbit.glitterhost.virtualHost}")
    String mqRabbitVirtualHost;
    @Value("${mq.rabbit.glitterhost.username}")
    String username;
    @Value("${mq.rabbit.glitterhost.password}")
    String password;

    public static final String GLITTERHOST_CONTAINER_FACTORY = "simpleRabbitListenerContainerFactory";

    public static final String GLITTERHOST_FIRST_FANOUT_QUEUE= "glitterhost.first.fanout.queue";
    public static final String GLITTERHOST_SECOND_FANOUT_QUEUE = "glitterhost.second.fanout.queue";


    // 创建mq连接
    @Bean(name = "connectionFactory")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(mqRabbitVirtualHost);
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setAddresses(address);

        return connectionFactory;
    }

//    // 创建监听容器工厂
//    @Bean(name="simpleRabbitListenerContainerFactory")
//    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        configurer.configure(factory, connectionFactory());
//        return factory;
//    }

    // 创建监听容器工厂
    @Bean(name="simpleRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        // 设置spring-amqp的ConnectionFactory。
        factory.setConnectionFactory(connectionFactory());
        //消息序列化类型
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //设置每个MessageListenerContainer将会创建的Consumer的最小数量，默认是1个。
        factory.setConcurrentConsumers(1);
        //设置每个MessageListenerContainer将会创建的Consumer的最大数量，默认等于最小数量。
        factory.setMaxConcurrentConsumers(1);
        //设置每次请求发送给每个Consumer的消息数量。
        //通过channel.basicQos(1)方法设置prefetch_count=1,这样mq针对channel信道每次只会从队列中只推送1条消息到客户端。
        //同理如果设置channel.basicQos(1)方法设置prefetch_count=10,那么mq针对channel信道每次会从队列中只推送10条消息到客户端。
        //不管每次推送多少条消息,在客户端手动确认这一个或“一批”消息前，mq都不会再次推送新的一个或“一批”消息来。
        factory.setPrefetchCount(2);
        //是否设置Channel的事务。
        factory.setChannelTransacted(false);
        //setTxSize：设置事务当中可以处理的消息数量。
        factory.setTxSize(1);
        //设置当rabbitmq收到nack/reject确认信息时的处理方式，设为true，扔回queue头部，设为false，丢弃。
        //factory.setDefaultRequeueRejected(true);
        // 设置确认模式手工确认(如果没有手动做任何确认,则消息在当前客户端会一直处于待确认状态,在当前消费者端处于阻塞状态,其他消费端轮询消费消息不受影响,如果该端停止服务或宕机,消息会重新返回队列排队)
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //实现ErrorHandler接口设置进去，所有未catch的异常都会由ErrorHandler处理。
        //factory.setErrorHandler();

        return factory;
    }

    @Bean
    public Queue glitterhostFirstFanoutQueue() {
        Queue glitterhostFirstFanoutQueue = new Queue(GLITTERHOST_FIRST_FANOUT_QUEUE);
        glitterhostFirstFanoutQueue.setShouldDeclare(false);
        return glitterhostFirstFanoutQueue;
    }

    @Bean
    public Queue glitterhostSecondFanoutQueue() {
        Queue glitterhostSecondFanoutQueue = new Queue(GLITTERHOST_FIRST_FANOUT_QUEUE);
        glitterhostSecondFanoutQueue.setShouldDeclare(false);
        return glitterhostSecondFanoutQueue;
    }

}