package com.glitter.spring.boot;

import com.glitter.spring.boot.factory.MouseServiceFactory;
import com.glitter.spring.boot.service.MouseService;

public class Main {

    public static void main(String[] args){
        // MouseServiceFactory可以设计为单例模式,或者getMouseServiceInstance方法设置为static静态方法,会更好
        MouseServiceFactory mouseServiceFactory = new MouseServiceFactory();
        MouseService hpMouseService = mouseServiceFactory.getMouseServiceInstance("hp");
        hpMouseService.click();
        MouseService dellMouseService = mouseServiceFactory.getMouseServiceInstance("dell");
        dellMouseService.click();
        MouseService huaweiMouseService = mouseServiceFactory.getMouseServiceInstance("huawei");
        huaweiMouseService.click();
    }

}
