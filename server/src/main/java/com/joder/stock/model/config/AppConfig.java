package com.joder.stock.model.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Joder 2020/8/19 20:22
 */
@Data
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private String token;
    private String api;
}
