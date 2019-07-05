package com.glitter.spring.boot.service.impl.hp;

import com.glitter.spring.boot.service.MouseService;
import org.springframework.stereotype.Service;

@Service
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
