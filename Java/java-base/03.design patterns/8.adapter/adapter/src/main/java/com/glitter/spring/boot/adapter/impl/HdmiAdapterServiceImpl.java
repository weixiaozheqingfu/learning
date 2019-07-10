package com.glitter.spring.boot.adapter.impl;

import com.glitter.spring.boot.adapter.TypecService;
import com.glitter.spring.boot.service.HdmiService;
import org.springframework.stereotype.Service;


/**
 * typec接口的hdmi转换器,用于对接外部设备接口是hdmi接口的外部设备
 */
@Service
public class HdmiAdapterServiceImpl implements TypecService {

    HdmiService hdmiService;

    public HdmiAdapterServiceImpl(HdmiService hdmiService) {
        this.hdmiService = hdmiService;
    }

    /**
     * 可以将外设的数据传输给电脑
     *
     * @param msg
     */
    @Override
    public void input(String msg) {
        hdmiService.hdmiInput(msg);
    }

    /**
     * 可以电脑数据传输给外设
     *
     * @param msg
     */
    @Override
    public void output(String msg) {
        hdmiService.hdmiOutput(msg);
    }

}
