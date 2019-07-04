package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.MouseService;

public class HpMouseServiceImpl implements MouseService{

    @Override
    public void move() {
        System.out.println("hp mouse move......");
    }

    @Override
    public void click() {
        System.out.println("hp mouse click......");
    }

    @Override
    public void dbclick() {
        System.out.println("hp mouse dbclick......");
    }

}
