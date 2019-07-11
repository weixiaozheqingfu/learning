package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.MicroUsbService;


/**
 * typec接口的microUsb转换器,用于对接外部设备接口是microUsb接口的外部设备
 */
public class MicroUsbServiceImpl implements MicroUsbService {

    /**
     * 可以将外设的数据传输给电脑
     *
     * @param msg
     */
    @Override
    public void microUsbInput(String msg) {
        System.out.println("microUsb外部设备传输数据到电脑，数据内容为："+msg);
    }

    /**
     * 可以电脑数据传输给外设
     *
     * @param msg
     */
    @Override
    public void microUsbOutput(String msg) {
        System.out.println("microUsb外部设备获取电脑数据到外部设备，获取数据内容为："+msg);
    }

}
