package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.mq.Glitterhost1FanoutExchangeSender;
import com.glitter.spring.boot.mq.Glitterhost2FanoutExchangeSender;
import com.glitter.spring.boot.mq.Glitterhost3FanoutExchangeSender;
import com.glitter.spring.boot.mq.Glitterhost4FanoutExchangeSender;
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
    @Autowired
    private Glitterhost2FanoutExchangeSender glitterhost2FanoutExchangeSender;
    @Autowired
    private Glitterhost3FanoutExchangeSender glitterhost3FanoutExchangeSender;
    @Autowired
    private Glitterhost4FanoutExchangeSender glitterhost4FanoutExchangeSender;

    @RequestMapping(value = "/send/first/{message}", method = RequestMethod.GET)
    public @ResponseBody
    String sendFirst(@PathVariable("message") final String message) {
        String result = glitterhost1FanoutExchangeSender.send(message);
        return result;
    }

    @RequestMapping(value = "/send/second/{message}", method = RequestMethod.GET)
    public @ResponseBody
    String sendSecond(@PathVariable("message") final String message) {
        String result = glitterhost2FanoutExchangeSender.send(message);
        return result;
    }

    @RequestMapping(value = "/send/third/{message}", method = RequestMethod.GET)
    public @ResponseBody
    String sendThird(@PathVariable("message") final String message) {
        String result = glitterhost3FanoutExchangeSender.send(message);
        return result;
    }

    @RequestMapping(value = "/send/fourth/{message}", method = RequestMethod.GET)
    public @ResponseBody
    String sendFourth(@PathVariable("message") final String message) {
        String result = glitterhost4FanoutExchangeSender.send(message);
        return result;
    }

}
