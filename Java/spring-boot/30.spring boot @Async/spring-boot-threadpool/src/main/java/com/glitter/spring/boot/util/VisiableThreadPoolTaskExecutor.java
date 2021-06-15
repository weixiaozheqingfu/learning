package com.glitter.spring.boot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 参考印象笔记中 多线程
 * https://blog.csdn.net/m0_37701381/article/details/81072774
 */
public class VisiableThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
    private static final Logger logger = LoggerFactory.getLogger(VisiableThreadPoolTaskExecutor.class);

    /**
     * 将任务总数、已完成数、活跃线程数，队列大小打印出来
     * 然后Override了父类的execute、submit等方法,在里面调用showThreadPoolInfo方法
     * 这样每次有任务被提交到线程池的时候，都会将当前线程池的基本情况打印到日志中
     * @param prefix
     */
    private void showThreadPoolInfo(String prefix) {
        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();
        if (null == threadPoolExecutor) {
            return;
        }
        logger.info("[{}], [{}],taskCount [{}], completedTaskCount [{}], activeCount [{}], queueSize [{}]",
                this.getThreadNamePrefix(),
                prefix,
                threadPoolExecutor.getTaskCount(),
                threadPoolExecutor.getCompletedTaskCount(),
                threadPoolExecutor.getActiveCount(),
                threadPoolExecutor.getQueue().size());
    }

    @Override
    public void execute(Runnable task) {
        showThreadPoolInfo("1.do execute");
        // 传入线程池之前先复制当前线程的MDC
        super.execute(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        showThreadPoolInfo("2.do execute");
        super.execute(task, startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        showThreadPoolInfo("1.do submit");
        return super.submit(task);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        showThreadPoolInfo("2.do submit");
        // 传入线程池之前先复制当前线程的MDC
        return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        showThreadPoolInfo("1.do submitListenable");
        return super.submitListenable(task);
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        showThreadPoolInfo("2.do submitListenable");
        return super.submitListenable(task);
    }
}

