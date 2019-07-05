package com.glitter.spring.boot;

import com.glitter.spring.boot.factory.PcServiceAbstractFactory;
import com.glitter.spring.boot.factory.PcServiceFactoryProducer;
import com.glitter.spring.boot.service.KeyboardService;
import com.glitter.spring.boot.service.MouseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestFactory {

    @Autowired
    PcServiceFactoryProducer pcServiceFactoryProducer;

    @Test
    public void factoryTest() {
//        String pcType = "hp";
//        String pcType = "dell";
        String pcType = "huawei";

        PcServiceAbstractFactory pcServiceAbstractFactory = pcServiceFactoryProducer.getPcServiceFactory(pcType);
        KeyboardService keyboardService = pcServiceAbstractFactory.getKeyboardServiceInstance();
        keyboardService.input("写字");
        MouseService mouseService = pcServiceAbstractFactory.getMouseServiceInstance();
        mouseService.click();

    }

}
