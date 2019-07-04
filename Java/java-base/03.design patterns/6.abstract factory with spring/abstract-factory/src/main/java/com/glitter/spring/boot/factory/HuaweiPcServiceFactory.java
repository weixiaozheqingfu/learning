package com.glitter.spring.boot.factory;

import com.glitter.spring.boot.service.KeyboardService;
import com.glitter.spring.boot.service.MouseService;
import com.glitter.spring.boot.service.impl.huawei.HuaweiKeyboardServiceImpl;
import com.glitter.spring.boot.service.impl.huawei.HuaweiMouseServiceImpl;

public class HuaweiPcServiceFactory extends PcServiceAbstractFactory{

    @Override
    public MouseService getMouseServiceInstance() {
        return new HuaweiMouseServiceImpl();
    }

    @Override
    public KeyboardService getKeyboardServiceInstance() {
        return new HuaweiKeyboardServiceImpl();
    }

}
