package com.glitter.spring.boot.context;

import com.glitter.spring.boot.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;

public class TerminateBean {

    @Autowired
    IUserInfoService userInfoService;

    @PreDestroy
    public void preDestroy() {
        System.out.println(userInfoService);
        System.out.println("TerminalBean is destroyed");
    }

}