package com.glitter.spring.boot.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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

    public static final String GLITTERHOST_FIRST_FANOUT_EXCHANGE = "g.first.fanout.exchange";
    public static final String GLITTERHOST_ORDER_EXPIRE_FANOUT_EXCHANGE = "g.order.expire.fanout.exchange";

    // 创建mq连接
    @Bean(name = "connectionFactory")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setAddresses(address);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(mqRabbitVirtualHost);

        // 开启发送者消息确认
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);

        return connectionFactory;
    }

    @Bean(name = "rabbitTemplate")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMandatory(true);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
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
    FanoutExchange glitterhostOrderExpireFanoutExchange() {
        FanoutExchange glitterhostOrderExpireFanoutExchange = new FanoutExchange(GLITTERHOST_ORDER_EXPIRE_FANOUT_EXCHANGE);
        glitterhostOrderExpireFanoutExchange.setShouldDeclare(false);
        return glitterhostOrderExpireFanoutExchange;
    }

}