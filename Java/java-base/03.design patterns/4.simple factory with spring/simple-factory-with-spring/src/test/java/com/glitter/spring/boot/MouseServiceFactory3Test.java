package com.glitter.spring.boot;

import com.glitter.spring.boot.factory.MouseServiceFactory3;
import com.glitter.spring.boot.service.MouseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MouseServiceFactory3Test {

    @Autowired
    MouseServiceFactory3 mouseServiceFactory3;

    @Test
    public void mouseServiceFactoryTest() {
        String type = "hp";
        MouseService mouseService = mouseServiceFactory3.getMouseServiceInstance(type);
        mouseService.click();
    }

}
