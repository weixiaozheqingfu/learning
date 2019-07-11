package com.glitter.spring.boot.service;

/**
 * Typec接口的外设
 */
public interface MobileTypecService {

    /**
     * 可以将外设的数据传输给电脑
     */
    void mobileTypecInput(String msg);

    /**
     * 可以电脑数据传输给外设
     */
    void mobileTypecOutput(String msg);

}
