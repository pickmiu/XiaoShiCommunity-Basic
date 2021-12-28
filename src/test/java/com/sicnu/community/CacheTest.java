package com.sicnu.community;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Component
public class CacheTest {

    @Data
    @AllArgsConstructor
    public static class User implements Serializable {
        private int id;
        private String name;
    }

    private Map<Integer, User> userMap = new ConcurrentHashMap();

    private User getUserInMap(int userId) {
        System.out.println("user db");
        return userMap.get(userId);
    }

    @Cacheable(value = "cacheTest", key = "#userId")
    public User getUser(int userId) {
        return getUserInMap(userId);
    }

    @CachePut(value = "cacheTest", key = "#user.id")
    public User updateUser(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    public void addUser(User user) {
        userMap.put(user.getId(), user);
    }
}