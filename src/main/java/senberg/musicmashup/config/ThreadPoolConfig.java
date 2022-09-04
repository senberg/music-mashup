package senberg.musicmashup.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ThreadPoolConfig {
    @Value("${musiccontrollerexecutor.core-pool-size}")
    private int musicControllerExecutorCorePoolSize;
    @Value("${musiccontrollerexecutor.max-pool-size}")
    private int musicControllerExecutorMaxPoolSize;
    @Value("${musiccontrollerexecutor.queue-capacity}")
    private int musicControllerExecutorQueueCapacity;

    @Bean
    public Executor musicControllerExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(musicControllerExecutorCorePoolSize);
        executor.setMaxPoolSize(musicControllerExecutorMaxPoolSize);
        executor.setQueueCapacity(musicControllerExecutorQueueCapacity);
        executor.setThreadNamePrefix("musicControllerExecutor-");
        executor.initialize();
        return executor;
    }
}
