package com.glitter.spi.impl;

import com.glitter.spi.WriteInterface;

/**
 * 毛笔实现
 * @author limengjun
 * @date 2018.08.10
 */
public class BrushPenImpl2 implements WriteInterface {

    @Override
    public void write(String words) {
        System.out.println("毛笔2写:"+words);
    }
}
