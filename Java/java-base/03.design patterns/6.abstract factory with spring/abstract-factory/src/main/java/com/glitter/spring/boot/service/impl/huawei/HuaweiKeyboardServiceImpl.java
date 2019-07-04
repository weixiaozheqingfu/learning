package com.glitter.spring.boot.service.impl.huawei;

import com.glitter.spring.boot.service.KeyboardService;

public class HuaweiKeyboardServiceImpl implements KeyboardService {

    @Override
    public void input(String str) {
        System.out.println("huawei keyboard input " + str +  "......");
    }

}
