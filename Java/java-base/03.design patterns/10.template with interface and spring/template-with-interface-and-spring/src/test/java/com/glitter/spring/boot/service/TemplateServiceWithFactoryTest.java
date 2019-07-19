package com.glitter.spring.boot.service;

import com.glitter.spring.boot.factory.TemplateServiceFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TemplateServiceWithFactoryTest {

    @Autowired
    TemplateServiceFactory templateServiceFactory;

    @Test
    public void test() {
        String demoType = "demo1";
        TemplateService templateService1 = templateServiceFactory.getTemplateServiceInstance(demoType);
        templateService1.templateMethod();

        System.out.println("");

        String demoType2 = "demo2";
        TemplateService templateService2 = templateServiceFactory.getTemplateServiceInstance(demoType2);
        templateService2.templateMethod();
    }

}
