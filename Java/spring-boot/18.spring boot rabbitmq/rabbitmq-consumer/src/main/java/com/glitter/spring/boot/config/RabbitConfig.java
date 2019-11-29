package com.glitter.spring.boot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @Description : fanout型消息配置
 * @Author : qin_zhao@kingdee.com
 * @Date : 2018-05-06 15:24
 */
@Configuration
public class RabbitConfig {

    @Value("${mq.rabbit.address}")
    String address;
    @Value("${mq.rabbit.username}")
    String username;
    @Value("${mq.rabbit.password}")
    String password;
    @Value("${mq.rabbit.queue.name}")
    String queuename;
    @Value("${mq.rabbit.virtualHost}")
    String mqRabbitVirtualHost;
    @Value("${mq.rabbit.exchange.name}")
    String exchangeName;

    // 创建mq连接
    @Bean(name = "connectionFactory")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(mqRabbitVirtualHost);
        connectionFactory.setPublisherConfirms(true);

        // 该方法配置多个host，在当前连接host down掉的时候会自动去重连后面的host
        connectionFactory.setAddresses(address);

        return connectionFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    // 必须是prototype类型
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }

    // Fanout交换器
    @Bean
    FanoutExchange fanoutExchange() {
        FanoutExchange fanoutExchange = new FanoutExchange(exchangeName);
        // 因为按照的我最优方案,普通用户glitter是不能声明创建队列和交换器的,只能读写,并且权限也是这么配置的,
        // 所以这里设置不需要声明交换器,只要去连接能读写就可以了,如果不设置,默认是true,那么对于glitter普通用户来说,会保持,因为它会尝试去声明这个交换器,而它又没有这个权限。
        // 如果你不想使用最优方案,那就别加这一行,使用默认值true,这样你的代码拥有随时可以声明创建或删除队列和交换器的权限和能力，只要你连接mq的用户有配置权限就可以。
        fanoutExchange.setShouldDeclare(false);
        return fanoutExchange;
    }

    // 队列A
    @Bean
    public Queue fanoutQueue() {
        Queue queue =  new Queue(queuename);
        // 因为按照的我最优方案,普通用户glitter是不能声明创建队列和交换器的,只能读写,并且权限也是这么配置的,
        // 所以这里设置不需要声明交换器,只要去连接能读写就可以了,如果不设置,默认是true,那么对于glitter普通用户来说,会保持,因为它会尝试去声明这个交换器,而它又没有这个权限。
        // 如果你不想使用最优方案,那就别加这一行,使用默认值true,这样你的代码拥有随时可以声明创建或删除队列和交换器的权限和能力，只要你连接mq的用户有配置权限就可以。
        queue.setShouldDeclare(false);
        return queue;
    }

    // 绑定对列到Fanout交换器
    @Bean
    Binding bindingFanoutExchange(Queue fanoutQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue).to(fanoutExchange);
    }

}