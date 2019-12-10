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
        //如果客户端对于"一批消息"逐一确认，那么会不断的有新的一条消息推送过来将这"一批"的数量补满。但是一般情况下设置"一批"都是固定的,不会再逐条确认了,
        //这一批应该有统一的处理,要么都确认成，要么都确认不成,这样再确认之前mq不会推送新的批次到客户端来,这样对业务来说也有了整体性，我们对代码的可控性也能做好。
        //举个例子,自己想一个场景,比如要通过mq传输一批数据，从A到B，当B完整的获取到这批数据后才开始业务处理，否则不处理业务。(A最好将这一批数据预取出来冻结，比如专门的表存储)
        //此时我们可以考虑将这批数据分页传输，提前通过另外一个交换器和队列发送一条mq消息，告诉B，我这有一批数据，数据的分页数据信息是什么，总共多少条多少页。B发送给A消息，说这个单子我收到了，发具体数据吧。
        //此时B给A发送数据，采用分页传输数据，生产者可以将同一页的数据的优先级设置为相同，比如页数就是优先级代码，每一条消息标注上它的优先级和页码，消费者可以设置预取数量等于每页条数，
        //判断当同一页码和优先级的消息到达每页条数时，就一次性确认这一批消息。否则就不确认。这个例子举的不好，因为一旦有数量不达标的情况就会造成队列阻塞。就当是一个启发吧。
        //即使针对上面的场景依然建议一条一条取，直到总条数取够了，再进行业务处理，否则就不进行业务处理。所以我实在想不到什么场景要预取多条，并且进行多条确认。
        //说了那么多，此处建议设置为1，并且消息也都是逐条确认，如果有批量推送的情况，要考虑好业务场景和确认逻辑。
        factory.setPrefetchCount(1);
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