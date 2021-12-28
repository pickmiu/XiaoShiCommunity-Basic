package com.sicnu.community.service.login;

import com.sicnu.community.pojo.User;
import com.sicnu.community.util.ValidationUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserService工厂
 * 目的：解除 Service 和 Controller的耦合，达到增加UserSerice功能实现而不需要修改Factory和Controller代码就可以使用
 * 
 * @author Tangliyi (2238192070@qq.com)
 */
@Component
public class UserServiceFactory {

    @Resource
    private List<UserService> userServiceList;

    private Map<String, UserService> userServiceMap;

    @PostConstruct
    private void init() {
        userServiceMap = new HashMap<>(userServiceList.size());
        for (UserService userService: userServiceList) {
            userServiceMap.put(userService.getLoginType(), userService);
        }
    }

    public UserService getUserService(String loginType) throws ValidationUtil.IllegalParamsException {
        UserService userService = userServiceMap.get(loginType);
        if (userService == null) {
            throw new ValidationUtil.IllegalParamsException("loginType illegal");
        }
        return userService;
    }

}
