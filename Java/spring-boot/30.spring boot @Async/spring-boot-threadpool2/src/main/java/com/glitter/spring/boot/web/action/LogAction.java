package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.domain.ILogDomain;
import com.glitter.spring.boot.service.IRsaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

@RestController
@RequestMapping("/login")
public class LogAction {

    private static final Logger logger = LoggerFactory.getLogger(LogAction.class);

    @Autowired
    private ILogDomain logDomain;

    @RequestMapping(value = "getFutureUserInfo", method = RequestMethod.GET)
    public ResponseResult<UserInfo> getFutureUserInfo(@RequestParam(required = false) String id) throws Exception{
        logger.info("LogAction.getFutureUserInfo开始................. ");
        Future<UserInfo> userInfoFuture = logDomain.getFutureUserInfo(id);
        return ResponseResult.success(userInfoFuture.get());
    }

    @RequestMapping(value = "getUserInfo", method = RequestMethod.GET)
    public ResponseResult<UserInfo> getUserInfo(@RequestParam(required = false) String id) throws Throwable {
        logger.info("LogAction.getUserInfo开始................. ");
        UserInfo result = logDomain.getUserInfo(id);
        return ResponseResult.success(result);
    }

    @RequestMapping(value = "execute", method = RequestMethod.GET)
    public ResponseResult execute(@RequestParam(required = false) String id) throws Exception {
        logger.info("LogAction.execute开始................. ");
        logDomain.execute(id);
        return ResponseResult.success(null);
    }
}

