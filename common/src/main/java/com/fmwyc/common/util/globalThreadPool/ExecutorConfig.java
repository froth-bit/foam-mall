package com.fmwyc.common.util.globalThreadPool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@Slf4j
public class ExecutorConfig {

//    private final static Log logger = LogFactory.getLog(ExecutorConfig.class);

    @Bean("new_Thread")
    public Executor carSocketExecutor() {
        //获取当前机器的核数
        int cpuNum = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(cpuNum);
        //配置最大线程数
        executor.setMaxPoolSize(cpuNum * 2);
        //配置队列大小
        executor.setQueueCapacity(100);
        //线程存活时间
        executor.setKeepAliveSeconds(60);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("new_Thread");
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
//        logger.info("线程池初始化成功！");
        log.info("线程池初始化成功！");
        return executor;
    }
}