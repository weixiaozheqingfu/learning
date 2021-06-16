package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.*;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@RestController
@RequestMapping("/threadpool")
public class ThreadpoolAction {

    private static final Logger logger = LoggerFactory.getLogger(ThreadpoolAction.class);

    @Autowired
    private IAsyncService asyncService;

    @RequestMapping(value = "execute1", method = RequestMethod.GET)
    public ResponseResult<UserInfo> execute1(@RequestParam(required = false) String id) throws Exception{
        logger.info("ThreadpoolAction.execute1开始................. ");
        Future<UserInfo> userInfoFuture = asyncService.execute1(id);
        logger.info("ThreadpoolAction.execute1结束................. ");
        return ResponseResult.success(userInfoFuture.get());
    }

    @RequestMapping(value = "execute2", method = RequestMethod.GET)
    public ResponseResult<String> execute2(@RequestParam(required = false) String id) throws Exception {

        logger.info("ThreadpoolAction.execute2开始................. ");
        Future<UserInfo> userInfoFuture1 = asyncService.execute1(id);
        Future<String> stringFuture2 = asyncService.execute2(id);
        while(true) {
            if(userInfoFuture1.isDone() && stringFuture2.isDone()) {
                logger.info("Task1 result: {}", userInfoFuture1.get().getAccount());
                logger.info("Task2 result: {}", stringFuture2.get());
                break;
            }
            Thread.sleep(1000);
        }
        logger.info("ThreadpoolAction.execute2结束................. ");
        return ResponseResult.success(userInfoFuture1.get().getAccount() + stringFuture2.get());
    }

    @RequestMapping(value = "execute3", method = RequestMethod.GET)
    public ResponseResult<String> execute3(@RequestParam(required = false) String id) throws Throwable {
        logger.info("ThreadpoolAction.execute3开始................. ");
        String result = asyncService.execute3(id);
        logger.info("ThreadpoolAction.execute3结束................. ");
        return ResponseResult.success(result);
    }

    @RequestMapping(value = "execute4", method = RequestMethod.GET)
    public ResponseResult execute4(@RequestParam(required = false) String id) throws Exception {
        logger.info("ThreadpoolAction.execute4开始................. ");
        asyncService.execute4(id);
        logger.info("ThreadpoolAction.execute4结束................. ");
        return ResponseResult.success(null);
    }

}
