package com.sicnu.community.dao;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.sicnu.community.dao.mapper.UserMapper;
import com.sicnu.community.pojo.User;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Repository
@CacheConfig(cacheNames = "basic-UserCache" )
public class UserDao {

    @Resource
    private UserMapper userMapper;

    @CachePut(key="#result.id", condition = "#result != null")
    public User findUserByEmail(String email) {
        return userMapper.findUserByEmail(email);
    }

    @Cacheable(key="#id")
    public User findUserById(int id) {
        return userMapper.findUserById(id);
    }

    @CachePut(key="#user.id", condition = "#result != null")
    public User addUser(User user) {
        if (userMapper.addUser(user) == 1) {
            return user;
        } else {
            return null;
        }
    }

    @CachePut(key="#user.id", condition = "#result != null")
    public User updateUser(User user) {
        if (userMapper.updateUser(user) > 0) {
            return user;
        } else {
            return null;
        }
    }
}
