package com.glitter.spring.boot;

import com.glitter.spring.boot.factory.MouseServiceFactory;
import com.glitter.spring.boot.service.MouseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MouseServiceFactoryTest {

    @Autowired
    MouseServiceFactory mouseServiceFactory;

    @Test
    public void mouseServiceFactoryTest() {
        String type = "hp";
        MouseService mouseService = mouseServiceFactory.getMouseServiceInstance(type);
        mouseService.click();
    }

}
