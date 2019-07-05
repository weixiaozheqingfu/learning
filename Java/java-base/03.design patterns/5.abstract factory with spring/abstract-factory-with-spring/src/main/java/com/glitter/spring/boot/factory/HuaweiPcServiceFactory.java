package com.glitter.spring.boot.factory;

import com.glitter.spring.boot.service.KeyboardService;
import com.glitter.spring.boot.service.MouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HuaweiPcServiceFactory extends PcServiceAbstractFactory{

    @Autowired
    MouseService huaweiMouseServiceImpl;
    @Autowired
    KeyboardService huaweiKeyboardServiceImpl;

    @Override
    public MouseService getMouseServiceInstance() {
        return huaweiMouseServiceImpl;
    }

    @Override
    public KeyboardService getKeyboardServiceInstance() {
        return huaweiKeyboardServiceImpl;
    }

}
