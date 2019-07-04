package com.glitter.spring.boot.factory;

import com.glitter.spring.boot.service.KeyboardService;
import com.glitter.spring.boot.service.MouseService;

public abstract class PcServiceAbstractFactory {

    public abstract MouseService getMouseServiceInstance();

    public abstract KeyboardService getKeyboardServiceInstance();

}
