package com.glitter.spi.impl;

import com.glitter.spi.CarInterface;

/**
 * 阿斯顿马丁汽车实现
 * @author limengjun
 * @date 2018.08.10
 */
public class AstonCarImpl implements CarInterface{

    @Override
    public void run() {
        System.out.println("阿斯顿马丁汽车启动......");
    }

    @Override
    public void stop() {
        System.out.println("阿斯顿马丁汽车停车......");
    }

}
