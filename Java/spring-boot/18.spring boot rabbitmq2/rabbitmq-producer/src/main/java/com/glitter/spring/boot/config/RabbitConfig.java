package com.glitter.spring.boot.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

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

    public static final String GLITTERHOST_FIRST_FANOUT_EXCHANGE = "glitterhost.first.fanout.exchange";
    public static final String GLITTERHOST_SECOND_FANOUT_EXCHANGE = "glitterhost.second.fanout.exchange";

    // 创建mq连接
    @Bean(name = "connectionFactory")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setAddresses(address);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(mqRabbitVirtualHost);
        connectionFactory.setPublisherConfirms(true);
        // 这个设置的作用是什么?
        connectionFactory.setPublisherConfirms(true);

        return connectionFactory;
    }

    // rabbit模版
    @Bean(name = "rabbitTemplate")
    //@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }

    // Fanout交换器
    @Bean
    FanoutExchange glitterhostFirstFanoutExchange() {
        FanoutExchange glitterhostFirstFanoutExchange = new FanoutExchange(GLITTERHOST_FIRST_FANOUT_EXCHANGE);
        glitterhostFirstFanoutExchange.setShouldDeclare(false);
        return glitterhostFirstFanoutExchange;
    }

    // Fanout交换器
    @Bean
    FanoutExchange glitterhostSecondFanoutExchange() {
        FanoutExchange glitterhostSecondFanoutExchange = new FanoutExchange(GLITTERHOST_SECOND_FANOUT_EXCHANGE);
        glitterhostSecondFanoutExchange.setShouldDeclare(false);
        return glitterhostSecondFanoutExchange;
    }

}