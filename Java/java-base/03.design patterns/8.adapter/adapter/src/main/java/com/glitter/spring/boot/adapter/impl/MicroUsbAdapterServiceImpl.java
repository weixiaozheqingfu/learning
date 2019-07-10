package com.glitter.spring.boot.adapter.impl;

import com.glitter.spring.boot.adapter.TypecAdapterService;
import com.glitter.spring.boot.service.MicroUsbService;
import org.springframework.stereotype.Service;


/**
 * typec接口的microUsb转换器,用于对接外部设备接口是microUsb接口的外部设备
 */
@Service
public class MicroUsbAdapterServiceImpl implements TypecAdapterService {

    MicroUsbService microUsbService;

    public MicroUsbAdapterServiceImpl(MicroUsbService microUsbService) {
        this.microUsbService = microUsbService;
    }

    /**
     * 可以将外设的数据传输给电脑
     *
     * @param msg
     */
    @Override
    public void input(String msg) {
        microUsbService.microUsbInput(msg);
    }

    /**
     * 可以电脑数据传输给外设
     *
     * @param msg
     */
    @Override
    public void output(String msg) {
        microUsbService.microUsbOutput(msg);
    }

}
