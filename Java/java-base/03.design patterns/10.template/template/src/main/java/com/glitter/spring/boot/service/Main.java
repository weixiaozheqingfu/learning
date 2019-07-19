package com.glitter.spring.boot.service;

import com.glitter.spring.boot.service.impl.Demo1ServiceImpl;
import com.glitter.spring.boot.service.impl.Demo2ServiceImpl;


/**
 模板模式和其他模式不冲突，并且可以和其他模式结合着用，尤其是和工厂模式。
 了解设计模式多了以后会发现，工厂模式就是基础设施一般的存在，很多模式都要结合着工厂模式使用才能发挥更好的效果。
 模板模式主要是提现了一种编排和约束的能力

 设计模式没有那么严格，有时候具体设计出来一种模式，你看起来可能会像多种设计模式，关键看你理解的侧重点是什么，不要拘泥于固定的东西，灵活运用即可。
 */
public class Main {

    public static void main(String[] args){
        test1();
    }

    private static void test1() {
        AbstractTemplateService templateService1 = new Demo1ServiceImpl();
        templateService1.templateMethod();

        System.out.println("");

        AbstractTemplateService templateService2 = new Demo2ServiceImpl();
        templateService2.templateMethod();
    }

}
