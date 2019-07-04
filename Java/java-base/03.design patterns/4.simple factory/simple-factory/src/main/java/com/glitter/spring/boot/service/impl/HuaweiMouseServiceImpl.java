package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.MouseService;

public class HuaweiMouseServiceImpl implements MouseService{

    @Override
    public void move() {
        System.out.println("huawei mouse move......");
    }

    @Override
    public void click() {
        System.out.println("huawei mouse click......");
    }

    @Override
    public void dbclick() {
        System.out.println("huawei mouse dbclick......");
    }

}
