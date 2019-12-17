package com.glitter.spring.boot.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitGlitterhostOrderExpireConfig {

    public static final String G_ORDER_EXPIRE_FANOUT_EXCHANGE = "g.order.expire.fanout.exchange";
    public static final String G_ORDER_EXPIRE_FANOUT_QUEUE = "g.order.expire.fanout.queue";

    public static final String x_dead_letter_exchange = "g.order.expire.dlx.direct.exchange";
    public static final String x_dead_letter_routing_key = "g.order.expire.dlx.key";
    public static final long x_message_ttl = 30000;

    public static final String G_ORDER_EXPIRE_DLX_DIRECT_EXCHANGE = "g.order.expire.dlx.direct.exchange";
    public static final String G_ORDER_EXPIRE_DLX_BINDING_KEY = "g.order.expire.dlx.key";
    public static final String G_ORDER_EXPIRE_DLX_DIRECT_QUEUE = "g.order.expire.dlx.direct.queue";

    @Bean
    FanoutExchange gOrderExpireFanoutExchange() {
        FanoutExchange exchange = new FanoutExchange(G_ORDER_EXPIRE_FANOUT_EXCHANGE);
        return exchange;
    }
    @Bean
    public Queue gOrderExpireFanoutQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", x_dead_letter_exchange);
        arguments.put("x-dead-letter-routing-key", x_dead_letter_routing_key);
        arguments.put("x-message-ttl", x_message_ttl);
        Queue queue = new Queue(G_ORDER_EXPIRE_FANOUT_QUEUE, true, false, false, arguments);
        return queue;
    }
    @Bean
    Binding bindingOrderExpireExchangeQueue() {
        return BindingBuilder.bind(gOrderExpireFanoutQueue()).to(gOrderExpireFanoutExchange());
    }


    @Bean
    DirectExchange gOrderExpireDlxDirectExchange() {
        DirectExchange exchange = new DirectExchange(G_ORDER_EXPIRE_DLX_DIRECT_EXCHANGE);
        return exchange;
    }
    @Bean
    public Queue gOrderExpireDlxDirectQueue() {
        Queue queue = new Queue(G_ORDER_EXPIRE_DLX_DIRECT_QUEUE);
        return queue;
    }
    @Bean
    Binding bindingOrderExpireDlxExchangeQueue() {
        return BindingBuilder.bind(gOrderExpireDlxDirectQueue()).to(gOrderExpireDlxDirectExchange()).with(G_ORDER_EXPIRE_DLX_BINDING_KEY);
    }

}