package com.glitter.spring.boot.factory;

import com.glitter.spring.boot.service.KeyboardService;
import com.glitter.spring.boot.service.MouseService;
import com.glitter.spring.boot.service.impl.dell.DellKeyboardServiceImpl;
import com.glitter.spring.boot.service.impl.dell.DellMouseServiceImpl;

public class DellPcServiceFactory extends PcServiceAbstractFactory{

    @Override
    public MouseService getMouseServiceInstance() {
        return new DellMouseServiceImpl();
    }

    @Override
    public KeyboardService getKeyboardServiceInstance() {
        return new DellKeyboardServiceImpl();
    }

}
