package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class AliPayServiceImpl implements IPayService {

    private static final Logger logger = LoggerFactory.getLogger(AliPayServiceImpl.class);

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
        logger.info("AliPayServiceImpl.pay方法执行,支付"+money+"元......");
    }

    @Override
    public Long accept(Long money) {
        logger.info("AliPayServiceImpl.accept方法执行,收款"+money+"元......");
        return money;
    }

}
