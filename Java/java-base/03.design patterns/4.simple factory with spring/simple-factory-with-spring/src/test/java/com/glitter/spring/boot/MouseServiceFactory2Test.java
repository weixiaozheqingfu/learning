package com.glitter.spring.boot;

import com.glitter.spring.boot.factory.MouseServiceFactory2;
import com.glitter.spring.boot.service.MouseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MouseServiceFactory2Test {

    @Autowired
    MouseServiceFactory2 mouseServiceFactory2;

    @Test
    public void mouseServiceFactoryTest() {
        String type = "hp";
        MouseService mouseService = mouseServiceFactory2.getMouseServiceInstance(type);
        mouseService.click();
    }

}
