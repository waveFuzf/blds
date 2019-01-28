package com.example.blds.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ExecutorConfig {
    private static int CORE_POOL_SIZE=5;
    private static int MAX_POOL_SIZE=1000;
    @Bean(name="taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor poolTaskExecutor=new ThreadPoolTaskExecutor();
        poolTaskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        poolTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        poolTaskExecutor.setQueueCapacity(200);
        poolTaskExecutor.setKeepAliveSeconds(30000);
        poolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        return poolTaskExecutor;
    }
}
