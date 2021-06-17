package com.glitter.spring.boot.domain.impl;

import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.domain.ILogDomain;
import com.glitter.spring.boot.service.impl.LogService;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Component
@Log4j2
public class LogDomain implements ILogDomain {

    @Autowired
    private LogService logService;

    @Override
    public Future<UserInfo> getFutureUserInfo (String id) {
        log.info("目标方法LogDomain.getFutureUserInfo开始执行........................");
        return logService.getFutureUserInfo(id);
    }

    @Override
    public UserInfo getUserInfo(String id) {
        log.info("[LogDomain.getUserInfo]我是------2-------");
        log.info("目标方法LogDomain.getFutureUserInfo开始执行........................");
        UserInfo userInfo = logService.getUserInfo(id);
//      int i = 3/0;
        return userInfo;
    }

    @Override
    public void execute(String id) {
        log.info("目标方法LogDomain.getFutureUserInfo开始执行........................");
        logService.execute(id);
    }

}