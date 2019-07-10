package com.glitter.spring.boot.service;

/**
 * Vga接口的外设
 */
public interface VgaService {

    /**
     * 可以将外设的数据传输给电脑
     */
    void vgaInput(String msg);

    /**
     * 可以电脑数据传输给外设
     */
    void vgaOutput(String msg);

}
