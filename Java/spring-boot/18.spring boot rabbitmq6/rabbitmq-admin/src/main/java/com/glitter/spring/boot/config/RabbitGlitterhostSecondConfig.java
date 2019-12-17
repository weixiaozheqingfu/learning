package com.glitter.spring.boot.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitGlitterhostSecondConfig {

    public static final String G_SECOND_FANOUT_EXCHANGE = "g.second.fanout.exchange";
    public static final String G_SECOND_FANOUT_QUEUE = "g.second.fanout.queue";

    public static final String x_dead_letter_exchange = "g.second.dlx.direct.exchange";
    public static final String x_dead_letter_routing_key = "g.second.dlx.key";

    public static final String G_SECOND_DLX_DIRECT_EXCHANGE = "g.second.dlx.direct.exchange";
    public static final String G_SECOND_DLX_BINDING_KEY = "g.second.dlx.key";
    public static final String G_SECOND_DLX_DIRECT_QUEUE = "g.second.dlx.direct.queue";


    @Bean
    FanoutExchange gSecondFanoutExchange() {
        FanoutExchange exchange = new FanoutExchange(G_SECOND_FANOUT_EXCHANGE);
        return exchange;
    }
    @Bean
    public Queue gSecondFanoutQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", x_dead_letter_exchange);
        arguments.put("x-dead-letter-routing-key", x_dead_letter_routing_key);
        Queue queue = new Queue(G_SECOND_FANOUT_QUEUE, true, false, false, arguments);
        return queue;
    }
    @Bean
    Binding bindingSecondExchangeQueue() {
        return BindingBuilder.bind(gSecondFanoutQueue()).to(gSecondFanoutExchange());
    }


    @Bean
    DirectExchange gSecondDlxDirectExchange() {
        DirectExchange exchange = new DirectExchange(G_SECOND_DLX_DIRECT_EXCHANGE);
        return exchange;
    }
    @Bean
    public Queue gSecondDlxDirectQueue() {
        Queue queue = new Queue(G_SECOND_DLX_DIRECT_QUEUE);
        return queue;
    }
    @Bean
    Binding bindingSecondDlxExchangeQueue() {
        return BindingBuilder.bind(gSecondDlxDirectQueue()).to(gSecondDlxDirectExchange()).with(G_SECOND_DLX_BINDING_KEY);
    }

}