package com.glitter.spring.boot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitmqRoothostConfig {

    @Value("${mq.rabbitmq.roothost.address}")
    String address;
    @Value("${mq.rabbitmq.roothost.virtualHost}")
    String mqRabbitVirtualHost;
    @Value("${mq.rabbitmq.roothost.username}")
    String username;
    @Value("${mq.rabbitmq.roothost.password}")
    String password;

    public static final String ROOTHOST_CONTAINER_FACTORY = "roothostContainerFactory";

    public static final String ROOTHOST_FIRST_FANOUT_EXCHANGE = "roothost.first.fanout.exchange";
    public static final String ROOTHOST_SECOND_FANOUT_EXCHANGE = "roothost.second.fanout.exchange";

    public static final String ROOTHOST_FIRST_FANOUT_QUEUE = "roothost.first.fanout.queue";
    public static final String ROOTHOST_SECOND_FANOUT_QUEUE = "roothost.second.fanout.queue";

    // 创建mq连接
    @Bean(name = "roothostConnectionFactory")
    @Primary
    public ConnectionFactory roothostConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(mqRabbitVirtualHost);
        connectionFactory.setPublisherConfirms(true);

        // 该方法配置多个host，在当前连接host down掉的时候会自动去重连后面的host
        connectionFactory.setAddresses(address);

        return connectionFactory;
    }

    @Bean(name="roothostContainerFactory")
    public SimpleRabbitListenerContainerFactory roothostContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer,
                                                                         @Qualifier("roothostConnectionFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    // Fanout(可以省略,消费者是不关心交换器的,这里声明是为了说明如果mq没有提前创建好交换器,root用户有自动创建的能力)
    @Bean
    FanoutExchange roothostFirstFanoutExchange() {
        FanoutExchange roothostFirstFanoutExchange = new FanoutExchange(ROOTHOST_FIRST_FANOUT_EXCHANGE);
        return roothostFirstFanoutExchange;
    }

    // 队列roothost.first.fanout.queue
    @Bean
    public Queue roothostFirstFanoutQueue() {
        Queue roothostFirstFanoutQueue =  new Queue(ROOTHOST_FIRST_FANOUT_QUEUE);
        return roothostFirstFanoutQueue;
    }

    // 绑定对列到Fanout交换器(可以省略,消费者是不关心交换器的,这里声明是为了说明如果mq没有提前创建好交换器,root用户有自动创建的能力)
    @Bean
    Binding bindingRoothostFirstFanoutExchange(Queue roothostFirstFanoutQueue, FanoutExchange roothostFirstFanoutExchange) {
        return BindingBuilder.bind(roothostFirstFanoutQueue).to(roothostFirstFanoutExchange);
    }

}