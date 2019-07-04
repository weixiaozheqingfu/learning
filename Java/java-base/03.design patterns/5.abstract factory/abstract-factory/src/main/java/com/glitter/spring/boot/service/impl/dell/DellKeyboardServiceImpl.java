package com.glitter.spring.boot.service.impl.dell;

import com.glitter.spring.boot.service.KeyboardService;

public class DellKeyboardServiceImpl implements KeyboardService {

    @Override
    public void input(String str) {
        System.out.println("dell keyboard input " + str +  "......");
    }

}
