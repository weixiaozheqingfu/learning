package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.mq.GlitterhostFirstFanoutExchangeSender;
import com.glitter.spring.boot.mq.GlitterhostSecondFanoutExchangeSender;
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
    private GlitterhostFirstFanoutExchangeSender glitterhostFirstFanoutExchangeSender;
    @Autowired
    private GlitterhostSecondFanoutExchangeSender glitterhostSecondFanoutExchangeSender;

    @RequestMapping(value = "/send/first/{name}/{message}", method = RequestMethod.GET)
    public @ResponseBody
    String sendFirst(@PathVariable("name") final String name, @PathVariable("message") final String message) {
        String result = glitterhostFirstFanoutExchangeSender.send(name, message);
        return result;
    }

    @RequestMapping(value = "/send/second/{name}/{message}", method = RequestMethod.GET)
    public @ResponseBody
    String sendSecond(@PathVariable("name") final String name, @PathVariable("message") final String message) {
        String result = glitterhostSecondFanoutExchangeSender.send(name, message);
        return result;
    }

}
