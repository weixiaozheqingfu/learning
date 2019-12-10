package com.glitter.spring.boot.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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

        // 开启发送者消息确认
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);

        return connectionFactory;
    }

    // 需要设置为多例,
    // 如果是单例,多个发送者线程引用同一个rabbitTemplate设置自己的回调ConfirmCallback时会报
    // Only one ConfirmCallback is supported by each RabbitTemplate
    @Bean(name = "rabbitTemplate")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        // 经过实践验证,此处是否设置mandatory对生产者消息确认没有什么作用
        // 这在于spring的处理,如果使用原始mq的api,这个参数一定是有作用的。
        // 对于spring来讲connectionFactory中的设置才是发挥作用的设置。
        template.setMandatory(true);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
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