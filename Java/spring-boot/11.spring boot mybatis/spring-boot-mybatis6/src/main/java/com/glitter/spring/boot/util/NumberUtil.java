package com.glitter.spring.boot.util;

/**
 * Description
 *
 * @author fxb
 * @date 2018-09-07
 */
public class NumberUtil {

    public static int getRandom(int min, int max) {
        //return (int) Math.floor(Math.random() * (max - min + 1)) + min;
        return (int) Math.floor(Math.random() * (max - min)) + min;
    }
}
