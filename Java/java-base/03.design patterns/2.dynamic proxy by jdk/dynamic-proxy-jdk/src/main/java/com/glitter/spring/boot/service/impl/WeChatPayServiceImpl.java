package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IPayService;

import java.util.Random;

public class WeChatPayServiceImpl implements IPayService {

    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void pay(Long money) {
        try {
            Thread.sleep(new Random().nextInt(10));
        } catch (InterruptedException e) {

        }
        System.out.println("WeChatPayServiceImpl.pay方法执行,支付"+money+"元......");
    }

    @Override
    public Long accept(Long money) {
        System.out.println("WeChatPayServiceImpl.accept方法执行,收款"+money+"元......");
        return money;
    }


}
