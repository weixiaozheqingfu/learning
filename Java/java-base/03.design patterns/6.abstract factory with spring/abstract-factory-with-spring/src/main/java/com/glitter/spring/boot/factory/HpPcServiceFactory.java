package com.glitter.spring.boot.factory;

import com.glitter.spring.boot.service.KeyboardService;
import com.glitter.spring.boot.service.MouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HpPcServiceFactory extends PcServiceAbstractFactory{

    @Autowired
    MouseService hpMouseServiceImpl;
    @Autowired
    KeyboardService hpKeyboardServiceImpl;

    @Override
    public MouseService getMouseServiceInstance() {
        return hpMouseServiceImpl;
    }

    @Override
    public KeyboardService getKeyboardServiceInstance() {
        return hpKeyboardServiceImpl;
    }

}
