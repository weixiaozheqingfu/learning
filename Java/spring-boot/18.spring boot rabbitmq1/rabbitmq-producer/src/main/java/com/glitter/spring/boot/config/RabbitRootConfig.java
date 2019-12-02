package com.glitter.spring.boot.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;

/**
 * @Description : fanout型消息配置
 * @Author : qin_zhao@kingdee.com
 * @Date : 2018-05-06 15:24
 */
@Configuration
public class RabbitRootConfig {

    @Value("${mq.rabbitmq.roothost.address}")
    String address;
    @Value("${mq.rabbitmq.roothost.username}")
    String username;
    @Value("${mq.rabbitmq.roothost.password}")
    String password;
    @Value("${mq.rabbitmq.roothost.virtualHost}")
    String mqRabbitVirtualHost;
    @Value("${mq.rabbitmq.roothost.exchange.name}")
    String exchangeName;

    // 创建mq连接
    @Bean(name = "connectionRoothostFactory")
    @Primary
    public ConnectionFactory connectionRoothostFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(mqRabbitVirtualHost);
        connectionFactory.setPublisherConfirms(true);

        // 该方法配置多个host，在当前连接host down掉的时候会自动去重连后面的host
        connectionFactory.setAddresses(address);

        return connectionFactory;
    }

    @Bean(name = "RabbitRoothostTemplate")
    @Primary
    // @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    // 必须是prototype类型
    public RabbitTemplate rabbitRoothostTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionRoothostFactory());
        return template;
    }

    // Fanout交换器
    @Bean
    FanoutExchange roothostFirstFanoutExchange() {
        FanoutExchange roothostFirstFanoutExchange = new FanoutExchange(exchangeName);
        return roothostFirstFanoutExchange;
    }

}