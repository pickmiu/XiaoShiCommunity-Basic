package com.sicnu.community.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.sicnu.community.enums.LoginTypeEnum;
import com.sicnu.community.pojo.User;
import com.sicnu.community.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sicnu.community.exception.ServiceExcption;
import com.sicnu.community.json.BackFrontMessage;
import com.sicnu.community.service.login.UserServiceFactory;
import com.sicnu.community.util.ValidationUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Slf4j
@RestController
public class LoginController {

    @Resource
    private UserServiceFactory userServiceFactory;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/anon/login")
    public BackFrontMessage login(@RequestParam String loginType, @RequestParam String username,
        @RequestParam String password, HttpServletResponse response) throws ValidationUtil.IllegalParamsException {
        check(loginType, username, password);
        User user = userServiceFactory.getUserService(loginType).login(username, password);
        if (user == null) {
            return new BackFrontMessage(500, "用户名或密码错误", null);
        } else {
            // 登录成功 签发token
            Map<String, Object> claims = new HashMap(1);
            claims.put("loginType", loginType);
            claims.put("username", username);
            String uniqueIdentifier = Integer.valueOf(user.getId()).toString();
            String token = jwtTokenUtil.generateToken(uniqueIdentifier, claims);
            log.info("[op:login] token={}", token);
            response.setHeader("token", token);
            return new BackFrontMessage(200, "登录成功", user);
        }
    }

    /**
     * 获取授权码
     *
     * @param platform
     * @return
     */
    @GetMapping("/getAuthorizationCode")
    public BackFrontMessage getAuthorizationCode(@RequestParam String platform)
        throws ValidationUtil.IllegalParamsException {
        return userServiceFactory.getUserService(LoginTypeEnum.DEFAULT.getValue()).getAuthorizationCode(platform);
    }

    /**
     * 获取用户详情
     * 
     * @param code
     * @return
     */
    @GetMapping("/anon/getUserDetailByCode")
    public BackFrontMessage getUserDetail(@RequestParam String code) throws ValidationUtil.IllegalParamsException {
        return userServiceFactory.getUserService(LoginTypeEnum.DEFAULT.getValue()).getUserDetail(code);
    }

    /**
     * @param loginType
     *            用户名的类型 "email"或"phone"
     * @param username
     * @param password
     * @param verifyCode
     * @return
     */
    @PostMapping("/anon/register")
    public BackFrontMessage register(@RequestParam String loginType, @RequestParam String username,
        @RequestParam String password, @RequestParam String verifyCode)
        throws ValidationUtil.IllegalParamsException, ServiceExcption {
        check(loginType, username, password);
        ValidationUtil.notNullAndEmpty(verifyCode);
        return userServiceFactory.getUserService(loginType).register(username, password, verifyCode);
    }

    /**
     * @param loginType
     * @param username
     *            发送地址 “手机号或邮箱”
     * @param operation
     *            操作 “注册”
     * @return
     */
    @GetMapping("/anon/sendVerifyCode")
    public BackFrontMessage sendVerifyCode(@RequestParam String loginType, @RequestParam String username,
        @RequestParam String operation) throws ValidationUtil.IllegalParamsException {
        check(loginType, username);
        return userServiceFactory.getUserService(loginType).sendVerifyCode(username, operation);
    }

    @PostMapping("/anon/newPassword")
    public BackFrontMessage changePassword(@RequestParam String loginType, @RequestParam String username,
        @RequestParam String newPassword, @RequestParam String verifyCode)
        throws ValidationUtil.IllegalParamsException, ServiceExcption {
        check(loginType, username, newPassword);
        ValidationUtil.notNullAndEmpty(verifyCode);
        return userServiceFactory.getUserService(loginType).changePassword(username, newPassword, verifyCode);
    }

    /**
     * 发送验证邮件 到指定邮箱
     * 
     * @param prefix
     *            前缀 @前面的部分
     * @return
     */
    @GetMapping("/sendVerifyEmail")
    public BackFrontMessage sendVerifyEmail(@RequestParam String prefix, @RequestParam String type)
        throws ValidationUtil.IllegalParamsException {
        String email = null;
        if ("student".equals(type)) {
            email = prefix + "@stu.sicnu.edu.cn";
        } else if ("other".equals(type)) {
            email = prefix + "@sicnu.edu.cn";
        } else {
            throw new ValidationUtil.IllegalParamsException("type valid");
        }
        ValidationUtil.email(email);

        return userServiceFactory.getUserService(LoginTypeEnum.DEFAULT.getValue()).sendVerifyEmail(email);
    }

    @GetMapping("/anon/validation")
    public BackFrontMessage validation(@RequestParam String code)
        throws ValidationUtil.IllegalParamsException, ServiceExcption {
        return userServiceFactory.getUserService(LoginTypeEnum.DEFAULT.getValue()).validation(code);
    }

    private void check(String loginType, String username) throws ValidationUtil.IllegalParamsException {
        if (LoginTypeEnum.EMAIL.getValue().equals(loginType)) {
            ValidationUtil.email(username);
        } else if (LoginTypeEnum.PHONE.getValue().equals(loginType)) {
            // 等手机模块接入修改
        }
    }

    private void check(String loginType, String username, String password)
        throws ValidationUtil.IllegalParamsException {
        check(loginType, username);
        ValidationUtil.password(password);
    }

}
