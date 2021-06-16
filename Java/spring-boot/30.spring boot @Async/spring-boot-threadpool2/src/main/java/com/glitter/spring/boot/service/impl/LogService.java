package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.domain.ILogDomain;
import com.glitter.spring.boot.service.IRsaService;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
@Log4j2
public class LogService implements ILogDomain {

    @Autowired
    private IRsaService rsaService;

    @Override
    public Future<UserInfo> getFutureUserInfo (String id) {
        log.info("目标方法LogService.getFutureUserInfo开始执行........................");
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount("gaoshanliushui");
        userInfo.setFullName("高山流水");
        AsyncResult<UserInfo> asyncResult = new AsyncResult<>(userInfo);

        String publicKey = rsaService.getPublicKey();
        log.info("目标方法LogService.getFutureUserInfo.publicKey:{}",publicKey);
        return asyncResult;
    }

    @Override
    public UserInfo getUserInfo(String id) {
        log.info("目标方法LogService.getUserInfo开始执行........................");
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount("gaogaogaogao");
        userInfo.setFullName("高高高高");
        return userInfo;
    }

    @Override
    public void execute(String id) {
        log.info("目标方法LogService.execute开始执行........................");
    }

}