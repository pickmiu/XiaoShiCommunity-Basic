package com.sicnu.community.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.Duration;
import java.util.Random;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Component
public class RedisUtil {

    @Resource
    private Random random;

    @Resource
    RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 使用随机生成的code作为key
     * @param value
     * @param timeout
     * @return
     */
    public String setRandomCodeAsKey(String value, Duration timeout) {
        String code;
        boolean exist;
        do {
            // 如果存在继续生成新的随机串，直到redis中不存在这个key为止
            code = DigestUtils.md5DigestAsHex(ByteUtil.int2Byte(random.nextInt()));
            exist = !redisTemplate.opsForValue().setIfAbsent(code, value,
                    timeout);
        } while (exist);
        return code;
    }

}