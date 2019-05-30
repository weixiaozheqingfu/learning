package com.glitter.spi.impl;

import com.glitter.spi.CarInterface;

/**
 * 奔驰汽车实现
 * @author limengjun
 * @date 2018.08.10
 */
public class AudiCarImpl2 implements CarInterface{

    @Override
    public void run() {
        System.out.println("奥迪车2启动......");
    }

    @Override
    public void stop() {
        System.out.println("奥迪车2停车......");
    }

}
