package com.sicnu.community.util;

import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Component
public class RandomUtil {
    @Resource
    private Random random;

    public String generateRandomCode(int length) {
        int bound = 10;
        for (int i = 0; i < length - 1; i++) {
            bound *= 10;
        }
        StringBuilder result = new StringBuilder(random.nextInt(bound) + "");
        while (result.length() < length) {
            result = new StringBuilder("0").append(result);
        }
        return result.toString();
    }
}
