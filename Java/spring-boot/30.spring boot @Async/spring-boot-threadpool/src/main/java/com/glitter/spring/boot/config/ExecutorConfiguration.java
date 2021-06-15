package com.glitter.spring.boot.config;

import com.glitter.spring.boot.constant.TraceConstants;
import com.glitter.spring.boot.util.ThreadMdcUtil;
import com.glitter.spring.boot.util.TraceLogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * 参考印象笔记中 多线程
 * https://blog.csdn.net/yangcheng33/article/details/80796129
 */
@Configuration
@EnableAsync
public class ExecutorConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorConfiguration.class);

    @Value("${async.executor.thread.core_pool_size}")
    private int corePoolSize;
    @Value("${async.executor.thread.max_pool_size}")
    private int maxPoolSize;
    @Value("${async.executor.thread.queue_capacity}")
    private int queueCapacity;
    @Value("${async.executor.thread.name.prefix}")
    private String namePrefix;

    @Bean(name = "asyncServiceExecutor")
    public Executor asyncServiceExecutor() {
        logger.info("start asyncServiceExecutor");
        // 对线程池进行包装，使之支持traceId透传
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor(){
            @Override
            public <T> Future<T> submit(Callable<T> task) {
                // 传入线程池之前先复制当前线程的MDC
                return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
            }
            @Override
            public void execute(Runnable task) {
                super.execute(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
            }
        };
        // 配置核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 配置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        // 配置队列大小
        executor.setQueueCapacity(queueCapacity);
        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(namePrefix);

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }

}
