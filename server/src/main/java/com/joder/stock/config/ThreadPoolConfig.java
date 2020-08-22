package com.joder.stock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author Joder 2020/8/22 15:44
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    @Primary
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        int num = Runtime.getRuntime().availableProcessors();
        return createThreadPoolTaskExecutor(num * 3, num * 5);
    }

    private ThreadPoolTaskExecutor createThreadPoolTaskExecutor(int size, int maxSize) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(maxSize);
        threadPoolTaskExecutor.setCorePoolSize(size);
        return threadPoolTaskExecutor;
    }
}
