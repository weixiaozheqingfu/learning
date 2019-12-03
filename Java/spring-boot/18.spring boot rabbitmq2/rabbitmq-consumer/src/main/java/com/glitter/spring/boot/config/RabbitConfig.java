package com.glitter.spring.boot.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
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

    // 创建监听容器工厂
    @Bean(name="simpleRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory());
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