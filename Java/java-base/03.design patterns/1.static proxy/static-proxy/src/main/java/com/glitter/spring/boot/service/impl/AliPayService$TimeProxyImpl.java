package com.glitter.spring.boot.service.impl;

public class AliPayService$TimeProxyImpl extends AliPayServiceImpl {

    @Override
    public void pay(Long money) {
        Long beginTime = System.currentTimeMillis();
        System.out.println("方法开始执行时间:" + beginTime);

        super.pay(money);

        Long endTime = System.currentTimeMillis();
        System.out.println("方法结束执行时间:" + endTime);
        System.out.println("方法执行耗时:" + (endTime - beginTime)+"毫秒");
        System.out.println();
    }

}
