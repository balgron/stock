package org.joder.stock.core.config;

import org.joder.stock.core.service.process.BackTestProcess;
import org.joder.stock.core.service.process.impl.DefaultBackTestProcessImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 回测处理配置
 *
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
