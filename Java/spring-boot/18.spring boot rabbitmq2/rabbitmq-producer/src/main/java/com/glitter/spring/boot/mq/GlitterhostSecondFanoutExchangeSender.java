package com.glitter.spring.boot.mq;

import com.glitter.spring.boot.config.GlitterhostRabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class GlitterhostSecondFanoutExchangeSender {

    private static final Logger logger = LoggerFactory.getLogger(GlitterhostSecondFanoutExchangeSender.class);

    @Resource(name = "glitterhostRabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Value(GlitterhostRabbitConfig.GLITTERHOST_SECOND_FANOUT_EXCHANGE)
    String exchange;

    public String send(String name, String message) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = simpleDateFormat.format(new Date());
        String sendMessage = "hello, " + name + ", " + message  + ", " + timeStr;

        rabbitTemplate.convertAndSend(exchange,null, sendMessage);
        logger.info("GlitterhostSecondFanoutExchangeSender rabbitTemplate:" + rabbitTemplate.toString());

        return "GlitterhostSecondFanoutExchangeSender send message to [" +  name + "] success (" + timeStr + ")";
    }

}
