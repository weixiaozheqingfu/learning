package com.glitter.spring.boot.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TemplateServiceTest {

    @Autowired
    TemplateService demo1ServiceImpl;
    @Autowired
    TemplateService demo2ServiceImpl;

    @Test
    public void test() {
        demo1ServiceImpl.templateMethod();
        System.out.println("");
        demo2ServiceImpl.templateMethod();
    }

}
