package com.glitter.spring.boot.adapter.impl;

import com.glitter.spring.boot.adapter.TypecService;
import com.glitter.spring.boot.service.MobileTypecService;
import org.springframework.stereotype.Service;


/**
 * typec接口的typec转换器,用于对接外部设备接口是typec接口的外部设备
 */
@Service
public class MobileTypecAdapterServiceImpl implements TypecService {

    MobileTypecService mobileTypecService;

    public MobileTypecAdapterServiceImpl(MobileTypecService mobileTypecService) {
        this.mobileTypecService = mobileTypecService;
    }

    /**
     * 可以将外设的数据传输给电脑
     *
     * @param msg
     */
    @Override
    public void input(String msg) {
        mobileTypecService.mobileTypecInput(msg);
    }

    /**
     * 可以电脑数据传输给外设
     *
     * @param msg
     */
    @Override
    public void output(String msg) {
        mobileTypecService.mobileTypecOutput(msg);
    }

}
