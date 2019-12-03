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
public class GlitterhostRabbitConfig {

    @Value("${mq.rabbit.glitterhost.address}")
    String address;
    @Value("${mq.rabbit.glitterhost.virtualHost}")
    String mqRabbitVirtualHost;
    @Value("${mq.rabbit.glitterhost.username}")
    String username;
    @Value("${mq.rabbit.glitterhost.password}")
    String password;

    public static final String GLITTERHOST_CONTAINER_FACTORY = "glitterhostContainerFactory";

    public static final String GLITTERHOST_FIRST_FANOUT_QUEUE= "glitterhost.first.fanout.queue";
    public static final String GLITTERHOST_SECOND_FANOUT_QUEUE = "glitterhost.second.fanout.queue";


    // 创建mq连接
    @Bean(name = "glitterHostConnectionFactory")
    public ConnectionFactory glitterHostConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(mqRabbitVirtualHost);
        connectionFactory.setPublisherConfirms(true);

        // 该方法配置多个host，在当前连接host down掉的时候会自动去重连后面的host
        connectionFactory.setAddresses(address);

        return connectionFactory;
    }

    @Bean(name="glitterhostContainerFactory")
    public SimpleRabbitListenerContainerFactory glitterhostContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, glitterHostConnectionFactory());
        return factory;
    }

    // 队列glitterhost.first.fanout.queue
    @Bean
    public Queue glitterhostFirstFanoutQueue() {
        Queue glitterhostFirstFanoutQueue = new Queue(GLITTERHOST_FIRST_FANOUT_QUEUE);
        // 因为按照的我最优方案,普通用户glitter是不能声明创建队列和交换器的,只能读写,并且权限也是这么配置的,
        // 所以这里设置不需要声明交换器,只要去连接能读写就可以了,如果不设置,默认是true,那么对于glitter普通用户来说,会保持,因为它会尝试去声明这个交换器,而它又没有这个权限。
        // 如果你不想使用最优方案,那就别加这一行,使用默认值true,这样你的代码拥有随时可以声明创建或删除队列和交换器的权限和能力，只要你连接mq的用户有配置权限就可以。
        glitterhostFirstFanoutQueue.setShouldDeclare(false);
        return glitterhostFirstFanoutQueue;
    }

    // 队列glitterhost.second.fanout.queue
    @Bean
    public Queue glitterhostSecondFanoutQueue() {
        Queue glitterhostSecondFanoutQueue = new Queue(GLITTERHOST_FIRST_FANOUT_QUEUE);
        // 因为按照的我最优方案,普通用户glitter是不能声明创建队列和交换器的,只能读写,并且权限也是这么配置的,
        // 所以这里设置不需要声明交换器,只要去连接能读写就可以了,如果不设置,默认是true,那么对于glitter普通用户来说,会保持,因为它会尝试去声明这个交换器,而它又没有这个权限。
        // 如果你不想使用最优方案,那就别加这一行,使用默认值true,这样你的代码拥有随时可以声明创建或删除队列和交换器的权限和能力，只要你连接mq的用户有配置权限就可以。
        glitterhostSecondFanoutQueue.setShouldDeclare(false);
        return glitterhostSecondFanoutQueue;
    }

}