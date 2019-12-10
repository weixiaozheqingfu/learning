package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.mq.Glitterhost1FanoutExchangeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description : 发送消息的controller
 * @Author : zq2599@gmail.com
 * @Date : 2018-05-06 15:15
 */
@RestController
@RequestMapping("/glitterhost")
public class GlitterhostRabbitSenderAction {

    @Autowired
    private Glitterhost1FanoutExchangeSender glitterhost1FanoutExchangeSender;

    @RequestMapping(value = "/send/first/{message}", method = RequestMethod.GET)
    public @ResponseBody
    String sendFirst(@PathVariable("message") final String message) {
        String result = glitterhost1FanoutExchangeSender.send(message);
        return result;
    }
}
