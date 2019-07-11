package com.glitter.spring.boot.service;

/**
 * MicroUsb接口的外设
 */
public interface MicroUsbService {

    /**
     * 可以将外设的数据传输给电脑
     */
    void microUsbInput(String msg);

    /**
     * 可以电脑数据传输给外设
     */
    void microUsbOutput(String msg);

}
