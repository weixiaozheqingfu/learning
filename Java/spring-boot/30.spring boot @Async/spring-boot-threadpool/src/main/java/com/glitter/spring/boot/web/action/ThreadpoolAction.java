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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.Future;


@Controller
@RequestMapping("/threadpool")
public class ThreadpoolAction {

    private static final Logger logger = LoggerFactory.getLogger(ThreadpoolAction.class);

    @Autowired
    private IAsyncService asyncService;

    @ResponseBody
    @RequestMapping(value = "execute1", method = RequestMethod.GET)
    public ResponseResult<UserInfo> execute1(@RequestParam(required = false) String id) {
        try {
            logger.info("ThreadpoolAction.execute1开始................. ");
            Future<UserInfo> userInfoFuture = asyncService.execute1(id);
            logger.info("ThreadpoolAction.execute1结束................. ");
            return ResponseResult.success(userInfoFuture.get());
        } catch (Exception e) {
            BusinessException ex = (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
            return ResponseResult.fail(ex.getCode(), ex.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "execute2", method = RequestMethod.GET)
    public ResponseResult<String> execute2(@RequestParam(required = false) String id) {
        try {
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
        } catch (Exception e) {
            BusinessException ex = (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
            return ResponseResult.fail(ex.getCode(), ex.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "execute3", method = RequestMethod.GET)
    public ResponseResult<String> execute3(@RequestParam(required = false) String id) {
        try {
            logger.info("ThreadpoolAction.execute3开始................. ");
            String result = asyncService.execute3(id);
            logger.info("ThreadpoolAction.execute3结束................. ");
            return ResponseResult.success(result);
        } catch (Exception e) {
            BusinessException ex = (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
            return ResponseResult.fail(ex.getCode(), ex.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "execute4", method = RequestMethod.GET)
    public ResponseResult execute4(@RequestParam(required = false) String id) {
        try {
            logger.info("ThreadpoolAction.execute4开始................. ");
            asyncService.execute4(id);
            logger.info("ThreadpoolAction.execute4结束................. ");
            return ResponseResult.success(null);
        } catch (Exception e) {
            BusinessException ex = (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
            return ResponseResult.fail(ex.getCode(), ex.getMessage());
        }
    }

}
