package com.glitter.spring.boot;

import com.glitter.spring.boot.service.IPayService;
import com.glitter.spring.boot.service.impl.*;

public class Main extends AliPayServiceImpl {

    public static void main(String[] args){
        // 使用继承的方式实现静态代理
        IPayService aliPayService$Proxy = new AliPayService$ProxyImpl();
        aliPayService$Proxy.pay(5L);

        IPayService weChatPayService$Proxy = new WeChatPayService$ProxyImpl();
        weChatPayService$Proxy.pay(6L);

        // 使用接口的方式实现静态代理
        IPayService aliPayService = new AliPayServiceImpl();
        IPayService payService$Proxy2 = new PayService$Proxy2Impl(aliPayService);
        payService$Proxy2.pay(10L);

        IPayService weChatPayService = new WeChatPayServiceImpl();
        payService$Proxy2 = new PayService$Proxy2Impl(weChatPayService);
        payService$Proxy2.pay(12L);
    }

}
