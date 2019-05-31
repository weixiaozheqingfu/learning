package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IPayService;

public class PayService$TimeProxyImpl implements IPayService {

    IPayService payService;

    public PayService$TimeProxyImpl(IPayService payService) {
        this.payService = payService;
    }

    @Override
    public void pay(Long money) {
        Long beginTime = System.currentTimeMillis();
        System.out.println("方法开始执行时间:" + beginTime);

        payService.pay(money);

        Long endTime = System.currentTimeMillis();
        System.out.println("方法结束执行时间:" + endTime);
        System.out.println("方法执行耗时:" + (endTime - beginTime) + "毫秒");
        System.out.println();
    }

}
