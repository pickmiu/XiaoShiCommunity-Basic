package com.sicnu.community.dao.mapper;

import com.sicnu.community.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Mapper
public interface UserMapper {

    User findUserByEmail(String email);

    User findUserById(int id);

    int addUser(User user);

    int updateUser(User user);
}