package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.AbstractTemplateService;
import org.springframework.stereotype.Service;

@Service
public class Demo2ServiceImpl extends AbstractTemplateService {

    /**
     * 原语操作1，算法中的必要步骤，父类无法确定如何真正实现，需要子类来实现
     */
    @Override
    protected void doPrimitiveOperation1() {
        System.out.println("Demo2ServiceImpl.doPrimitiveOperation1......");
    }

    /**
     * 原语操作2，算法中的必要步骤，父类无法确定如何真正实现，需要子类来实现
     */
    @Override
    protected void doPrimitiveOperation2() {
        System.out.println("Demo2ServiceImpl.doPrimitiveOperation2......");
    }

    /**
     * 钩子方法,
     * 可以选择完全重写该方法,
     * 也可以选择执行自己逻辑的前后,执行父类的方法
     *
     * 具体看业务需要
     */
    @Override
    protected void hookOperation1(){
        super.commonOperation();
        System.out.println("Demo2ServiceImpl.hookOperation1......");
    }

}
