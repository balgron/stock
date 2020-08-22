package com.joder.stock.config;

import com.joder.stock.strategy.service.process.BackTestProcess;
import com.joder.stock.strategy.service.process.impl.DefaultBackTestProcessImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Joder 2020/8/16 9:35
 */
@Configuration
public class BackTestConfig {

    @Bean
    @ConditionalOnMissingBean(BackTestProcess.class)
    public BackTestProcess backTestProcess() {
        return new DefaultBackTestProcessImpl();
    }
}
