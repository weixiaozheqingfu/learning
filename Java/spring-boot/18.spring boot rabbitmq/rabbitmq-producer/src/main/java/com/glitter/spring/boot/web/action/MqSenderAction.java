package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.mq.FanoutSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description : 发送消息的controller
 * @Author : zq2599@gmail.com
 * @Date : 2018-05-06 15:15
 */
@RestController
public class MqSenderAction {

    @Autowired
    private FanoutSender fanoutSender;

    @Value("${mq.rabbit.exchange.name}")
    String exchangeName;

    @RequestMapping(value = "/send/{name}/{message}", method = RequestMethod.GET)
    public @ResponseBody
    String send(@PathVariable("name") final String name, @PathVariable("message") final String message) {
        String result = fanoutSender.send(name, message);
        return result;
    }
}
