package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IPayService;

public class PayService$LogProxyImpl implements IPayService {

    IPayService payService;

    public PayService$LogProxyImpl(IPayService payService) {
        this.payService = payService;
    }

    @Override
    public void pay(Long money) {
        System.out.println(payService.getClass().getName() + ".pay方法输入参数:" + money);
        payService.pay(money);
        System.out.println(payService.getClass().getName() + ".pay方法执行完毕");
    }

    @Override
    public Long accept(Long money) {
        return null;
    }

}
