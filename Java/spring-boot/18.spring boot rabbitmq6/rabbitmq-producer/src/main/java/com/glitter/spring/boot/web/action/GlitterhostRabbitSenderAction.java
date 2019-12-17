package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.bean.mq.OrderInfo;
import com.glitter.spring.boot.mq.Glitterhost1FanoutExchangeSender;
import com.glitter.spring.boot.mq.GlitterhostOrderExpireFanoutExchangeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/glitterhost")
public class GlitterhostRabbitSenderAction {

    @Autowired
    private GlitterhostOrderExpireFanoutExchangeSender glitterhostOrderExpireFanoutExchangeSender;
    @Autowired
    private Glitterhost1FanoutExchangeSender glitterhost1FanoutExchangeSender;

    @RequestMapping(value = "/send/order/{orderId}", method = RequestMethod.GET)
    public @ResponseBody
    String sendOrder(@PathVariable("orderId") final Long orderId) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(orderId);

        String result = glitterhostOrderExpireFanoutExchangeSender.send(orderInfo);
        return result;
    }

    @RequestMapping(value = "/send/first/{userId}", method = RequestMethod.GET)
    public @ResponseBody
    String sendFirst(@PathVariable("userId") final Long userId) {

        // 构建消息信息(假定是根据userId查询)
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setAccount("gaoshanliushui" + userId);
        userInfo.setNickName("高山流水" + userId);
        userInfo.setEmail("xxxyyy@qq.com" + userId);
        userInfo.setAge(30 + Integer.valueOf(String.valueOf(userId)));
        userInfo.setDeleteFlag(false);
        userInfo.setCreateTime(new Date());

        String result = glitterhost1FanoutExchangeSender.send(userInfo);
        return result;
    }
}
