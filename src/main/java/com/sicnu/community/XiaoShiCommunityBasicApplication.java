package com.sicnu.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@EnableCaching 启用缓存
public class XiaoShiCommunityBasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoShiCommunityBasicApplication.class, args);
    }

}
