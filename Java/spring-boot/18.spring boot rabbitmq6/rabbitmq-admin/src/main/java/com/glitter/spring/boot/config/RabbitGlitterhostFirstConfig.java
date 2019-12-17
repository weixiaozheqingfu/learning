package com.glitter.spring.boot.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitGlitterhostFirstConfig {

    public static final String G_FIRST_FANOUT_EXCHANGE = "g.first.fanout.exchange";
    public static final String G_FIRST_FANOUT_QUEUE = "g.first.fanout.queue";

    public static final String x_dead_letter_exchange = "g.first.dlx.direct.exchange";
    public static final String x_dead_letter_routing_key = "g.first.dlx.key";

    public static final String G_FIRST_DLX_DIRECT_EXCHANGE = "g.first.dlx.direct.exchange";
    public static final String G_FIRST_DLX_BINDING_KEY = "g.first.dlx.key";
    public static final String G_FIRST_DLX_DIRECT_QUEUE = "g.first.dlx.direct.queue";


    @Bean
    FanoutExchange gFirstFanoutExchange() {
        FanoutExchange exchange = new FanoutExchange(G_FIRST_FANOUT_EXCHANGE);
        exchange.setDelayed(false);
        return exchange;
    }
    @Bean
    public Queue gFirstFanoutQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", x_dead_letter_exchange);
        arguments.put("x-dead-letter-routing-key", x_dead_letter_routing_key);
        arguments.put("x-max-priority", 10);
        Queue queue = new Queue(G_FIRST_FANOUT_QUEUE, true, false, false, arguments);
        return queue;
    }
    @Bean
    Binding bindingFirstExchangeQueue() {
        return BindingBuilder.bind(gFirstFanoutQueue()).to(gFirstFanoutExchange());
    }


    @Bean
    DirectExchange gFirstDlxDirectExchange() {
        DirectExchange exchange = new DirectExchange(G_FIRST_DLX_DIRECT_EXCHANGE);
        return exchange;
    }
    @Bean
    public Queue gFirstDlxDirectQueue() {
        Queue queue = new Queue(G_FIRST_DLX_DIRECT_QUEUE);
        return queue;
    }
    @Bean
    Binding bindingFirstDlxExchangeQueue() {
        return BindingBuilder.bind(gFirstDlxDirectQueue()).to(gFirstDlxDirectExchange()).with(G_FIRST_DLX_BINDING_KEY);
    }

}