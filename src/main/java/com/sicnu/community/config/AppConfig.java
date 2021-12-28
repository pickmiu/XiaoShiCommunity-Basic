package com.sicnu.community.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 全局配置
 * @author Tangliyi (2238192070@qq.com)
 */
@Configuration
public class AppConfig {

    @Bean
    public Config config() {
        return ConfigService.getAppConfig();
    }
}