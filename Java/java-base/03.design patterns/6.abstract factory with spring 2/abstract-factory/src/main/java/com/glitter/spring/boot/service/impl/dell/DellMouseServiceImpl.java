package com.glitter.spring.boot.service.impl.dell;

import com.glitter.spring.boot.service.MouseService;

public class DellMouseServiceImpl implements MouseService{

    @Override
    public void move() {
        System.out.println("dell mouse move......");
    }

    @Override
    public void click() {
        System.out.println("dell mouse click......");
    }

    @Override
    public void dbclick() {
        System.out.println("dell mouse dbclick......");
    }

}
