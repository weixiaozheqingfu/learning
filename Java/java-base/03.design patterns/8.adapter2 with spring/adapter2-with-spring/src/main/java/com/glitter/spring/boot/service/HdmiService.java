package com.glitter.spring.boot.service;

/**
 * Hdmi接口的外设
 */
public interface HdmiService {

    /**
     * 可以将外设的数据传输给电脑
     */
    void hdmiInput(String msg);

    /**
     * 可以电脑数据传输给外设
     */
    void hdmiOutput(String msg);

}
