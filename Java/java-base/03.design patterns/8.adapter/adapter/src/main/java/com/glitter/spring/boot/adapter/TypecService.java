package com.glitter.spring.boot.adapter;

/**
 * 电脑针对外设提供了typec接口,外部设备与电脑交互数据就必须通过TypecService接口进行
 *
 * 而各个adapter适配器实现类其实就是各种适配数据线，
 * 接电脑的这一头是标准的typec接口(适配器实现类的数据线在内部的这一头实现了标准的电脑TypecService接口),
 * 接外设的可能是各种接口了(适配器实现类的数据线在内部的另外一头注入了其他接口来进行接口适配)。
 */
public interface TypecService {

    /**
     * 可以将外设的数据传输给电脑
     */
    void input(String msg);

    /**
     * 可以电脑数据传输给外设
     */
    void output(String msg);

}
