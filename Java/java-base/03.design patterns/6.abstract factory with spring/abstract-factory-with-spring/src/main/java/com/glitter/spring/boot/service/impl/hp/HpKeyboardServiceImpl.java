package com.glitter.spring.boot.service.impl.hp;

import com.glitter.spring.boot.service.KeyboardService;
import org.springframework.stereotype.Service;

@Service
public class HpKeyboardServiceImpl implements KeyboardService {

    @Override
    public void input(String str) {
        System.out.println("hp keyboard input " + str +  "......");
    }

}
