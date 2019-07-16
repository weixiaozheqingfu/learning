package com.glitter.spring.boot.service.impl.decorator;

import com.glitter.spring.boot.service.IPayService;

public class LogPayServiceDecoratorImpl extends AbstractPayServiceDecoratorImpl{

    public LogPayServiceDecoratorImpl(IPayService payService) {
        super(payService);
    }

    @Override
    public void pay(Long money) {
        System.out.println(payServiceTarget.getClass().getName() + ".pay方法输入参数:" + money);
        super.pay(money);
        System.out.println(payServiceTarget.getClass().getName() + ".pay方法执行完毕");
    }

}
