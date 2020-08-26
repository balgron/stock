package org.joder.stock.config;

import org.joder.stock.model.config.AppConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 请求http接口配置
 *
 * @author Joder 2020/8/19 21:52
 */
@Configuration
@EnableConfigurationProperties(AppConfig.class)
public class HttpConfig {

    @Bean
    public AppConfig appConfig() {
        return new AppConfig();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .exchangeStrategies(
                        ExchangeStrategies.builder()
                                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                                .build()
                )
                .build();
    }
}
