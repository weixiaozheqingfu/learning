package com.glitter.spring.boot.service.impl.dell;

import com.glitter.spring.boot.service.KeyboardService;
import org.springframework.stereotype.Service;

@Service
public class DellKeyboardServiceImpl implements KeyboardService {

    @Override
    public void input(String str) {
        System.out.println("dell keyboard input " + str +  "......");
    }

}
