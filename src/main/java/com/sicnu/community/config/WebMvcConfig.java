package com.sicnu.community.config;

import com.sicnu.community.interceptor.JsonWebTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author Tangliyi (2238192070@qq.com)
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private JsonWebTokenInterceptor jsonWebTokenInterceptor;

    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jsonWebTokenInterceptor);
    }
}