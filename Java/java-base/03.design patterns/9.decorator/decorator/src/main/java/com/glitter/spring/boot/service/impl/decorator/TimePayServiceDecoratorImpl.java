package com.glitter.spring.boot.service.impl.decorator;

import com.glitter.spring.boot.service.IPayService;

public class TimePayServiceDecoratorImpl extends AbstractPayServiceDecoratorImpl{

    public TimePayServiceDecoratorImpl(IPayService payService) {
        super(payService);
    }

    @Override
    public void pay(Long money) {
        System.out.println("方法执行开始时间:" + System.currentTimeMillis());
        super.pay(money);
        System.out.println("方法执行结束时间:" + System.currentTimeMillis());
    }

}
