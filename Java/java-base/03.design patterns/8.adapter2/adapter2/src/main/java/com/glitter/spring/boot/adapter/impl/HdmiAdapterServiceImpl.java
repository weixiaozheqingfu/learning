package com.glitter.spring.boot.adapter.impl;

import com.glitter.spring.boot.adapter.TypecAdapterService;
import com.glitter.spring.boot.service.HdmiService;


/**
 * typec接口的hdmi转换器,用于对接外部设备接口是hdmi接口的外部设备
 *
 *
 * 要知道不同的适配器中的handler是完全不一样的接口对象,他们的接口定义的接口名字，参数顺序，个数可能都是不一样的。
 * 我这里举例只是名称不一样而已。
 * 比如参数多的时候，HdmiService的接口可能只要透传一个参数就能正常完成工作，而VgaService接口可能就要把所有参数都透传过去。
 * 这个是由HdmiService和VgaService接口等各自的接口定义自己决定的。
 */
public class HdmiAdapterServiceImpl implements TypecAdapterService {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof HdmiService);
    }

    /**
     * 可以将外设的数据传输给电脑
     *
     * @param msg
     */
    @Override
    public void input(String msg, Object handler) {
        HdmiService hdmiService = (HdmiService) handler;
        hdmiService.hdmiInput(msg);
    }

    /**
     * 可以电脑数据传输给外设
     *
     * @param msg
     */
    @Override
    public void output(String msg, Object handler) {
        HdmiService hdmiService = (HdmiService) handler;
        hdmiService.hdmiOutput(msg);
    }

}
