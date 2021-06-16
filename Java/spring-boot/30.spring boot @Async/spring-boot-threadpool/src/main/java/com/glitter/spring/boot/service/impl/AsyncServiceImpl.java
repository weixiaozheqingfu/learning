package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IAsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * 异步线程的异常日志需要自行捕获并使用logger打印
 */
@Service
public class AsyncServiceImpl implements IAsyncService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncServiceImpl.class);

    @Override
    @Async("visiableExecutor")
    public Future<UserInfo> execute1(String id) throws InterruptedException {
        logger.info("start execute1");
        logger.info("doing execute1.............");
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount("gaoshan");
        userInfo.setFullName("高山");
        AsyncResult<UserInfo> asyncResult = new AsyncResult<>(userInfo);
        Thread.sleep(5000);
        logger.info("end execute1");
        return asyncResult;
    }

    @Override
    @Async("visiableExecutor")
    public Future<String> execute2(String id) {
        logger.info("start execute2");
        logger.info("doing execute2.............");
        String result = "这是execute2返回结果";
        AsyncResult<String> asyncResult = new AsyncResult<>(result);
        logger.info("end execute2");
        return asyncResult;
    }

    @Override
    @Async("visiableExecutor")
    public String execute3(String id) throws Throwable {
        logger.info("start execute3");

//        if (true) {
//            throw new Throwable("-1");
//        }
//        if (true) {
//            throw new Error("-1");
//        }
//        if (true) {
//            throw new BusinessException("-1","业务异常");
//        }
//        if (true) {
//            throw new Exception("null");
//        }
//        if (true) {
//            throw new RuntimeException("1");
//        }
//        int i = 3 / 0;
        logger.info("doing execute3.............");
        logger.info("end execute3");
        return "这是excute3返回结果";
    }

    @Override
    @Async("asyncServiceExecutor")
    public void execute4(String id) throws InterruptedException {
        logger.info("start execute4");
//        int i = 3 / 0;
        logger.info("doing execute4.............");
        Thread.sleep(5000);
        logger.info("end execute4");
    }

}