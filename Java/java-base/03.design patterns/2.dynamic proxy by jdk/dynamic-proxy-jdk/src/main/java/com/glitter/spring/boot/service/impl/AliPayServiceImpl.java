package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IPayService;

import java.util.Random;

public class AliPayServiceImpl implements IPayService {

    @Override
    public void pay(Long money) {
        try {
            Thread.sleep(new Random().nextInt(10));
        } catch (InterruptedException e) {

        }
        System.out.println("AliPayServiceImpl.pay方法执行,支付"+money+"元......");
    }

}
