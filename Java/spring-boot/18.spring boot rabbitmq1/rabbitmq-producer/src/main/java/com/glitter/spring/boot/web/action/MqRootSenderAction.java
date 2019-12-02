package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.mq.RoothostFirstFanoutExchangeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description : 发送消息的controller
 * @Author : zq2599@gmail.com
 * @Date : 2018-05-06 15:15
 */
@RestController
@RequestMapping("/roothost")
public class MqRootSenderAction {

    @Autowired
    private RoothostFirstFanoutExchangeSender roothostFirstFanoutExchangeSender;

    @RequestMapping(value = "/send/{name}/{message}", method = RequestMethod.GET)
    public @ResponseBody
    String send(@PathVariable("name") final String name, @PathVariable("message") final String message) {
        String result = roothostFirstFanoutExchangeSender.send(name, message);
        return result;
    }
}
