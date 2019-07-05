package com.glitter.spring.boot.factory;

import com.glitter.spring.boot.service.KeyboardService;
import com.glitter.spring.boot.service.MouseService;
import com.glitter.spring.boot.service.impl.hp.HpKeyboardServiceImpl;
import com.glitter.spring.boot.service.impl.hp.HpMouseServiceImpl;

public class HpPcServiceFactory extends PcServiceAbstractFactory{

    @Override
    public MouseService getMouseServiceInstance() {
        return new HpMouseServiceImpl();
    }

    @Override
    public KeyboardService getKeyboardServiceInstance() {
        return new HpKeyboardServiceImpl();
    }

}
