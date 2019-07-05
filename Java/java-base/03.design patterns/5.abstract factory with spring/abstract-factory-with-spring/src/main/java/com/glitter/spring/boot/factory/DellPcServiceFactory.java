package com.glitter.spring.boot.factory;

import com.glitter.spring.boot.service.KeyboardService;
import com.glitter.spring.boot.service.MouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DellPcServiceFactory extends PcServiceAbstractFactory{

    @Autowired
    MouseService dellMouseServiceImpl;
    @Autowired
    KeyboardService dellKeyboardServiceImpl;

    @Override
    public MouseService getMouseServiceInstance() {
        return dellMouseServiceImpl;
    }

    @Override
    public KeyboardService getKeyboardServiceInstance() {
        return dellKeyboardServiceImpl;
    }

}
