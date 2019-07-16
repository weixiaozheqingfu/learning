package com.glitter.spring.boot.service.impl.decorator;

import com.glitter.spring.boot.service.IPayService;

public abstract class AbstractPayServiceDecoratorImpl implements IPayService {

    IPayService payServiceTarget;

    public AbstractPayServiceDecoratorImpl(IPayService payService) {
        this.payServiceTarget = payService;
    }

    @Override
    public void pay(Long money) {
        payServiceTarget.pay(money);
    }

}
