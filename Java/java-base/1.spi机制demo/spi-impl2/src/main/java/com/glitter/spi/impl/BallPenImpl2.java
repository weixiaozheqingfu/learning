package com.glitter.spi.impl;

import com.glitter.spi.WriteInterface;

/**
 * 圆珠笔实现
 * @author limengjun
 * @date 2018.08.10
 */
public class BallPenImpl2 implements WriteInterface {

    @Override
    public void write(String words) {
        System.out.println("圆珠笔2写:"+words);
    }
}
