package com.glitter.spring.boot.service;

import com.glitter.spring.boot.service.impl.AliPayServiceImpl;
import com.glitter.spring.boot.service.impl.WeChatPayServiceImpl;
import com.glitter.spring.boot.service.impl.decorator.LogPayServiceDecoratorImpl;
import com.glitter.spring.boot.service.impl.decorator.TimePayServiceDecoratorImpl;


/**
 * 1.其实所谓的装饰模式和静态代理模式基本一样。
 * 2.这里的抽象装饰类AbstractPayServiceDecoratorImpl,这一层实际是可以省略的。
 */
public class Main {

    public static void main(String[] args){
        test1();
        test2();
    }

    private static void test1() {
        IPayService aliPayService = new AliPayServiceImpl();
        IPayService payService1 = new LogPayServiceDecoratorImpl(aliPayService);
        payService1.pay(10L);

        IPayService weChatPayService = new WeChatPayServiceImpl();
        IPayService payService2 = new LogPayServiceDecoratorImpl(weChatPayService);
        payService2.pay(20L);
    }

    private static void test2() {
        IPayService aliPayService = new AliPayServiceImpl();
        IPayService aliPayServiceWithTime = new TimePayServiceDecoratorImpl(aliPayService);
        IPayService aliPayServiceWithTimeAndLog = new LogPayServiceDecoratorImpl(aliPayServiceWithTime);
        aliPayServiceWithTimeAndLog.pay(30L);

        IPayService weChatPayService = new WeChatPayServiceImpl();
        IPayService weChatPayServiceWithTime = new TimePayServiceDecoratorImpl(weChatPayService);
        IPayService weChatPayServiceWithTimeAndLog = new LogPayServiceDecoratorImpl(weChatPayServiceWithTime);
        weChatPayServiceWithTimeAndLog.pay(40L);
    }


}
