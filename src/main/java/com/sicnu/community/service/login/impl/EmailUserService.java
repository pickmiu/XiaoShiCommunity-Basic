package com.sicnu.community.service.login.impl;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.sicnu.community.config.ApolloConfig;
import com.sicnu.community.json.OperationDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.sicnu.community.dao.UserDao;
import com.sicnu.community.json.BackFrontMessage;
import com.sicnu.community.json.MailMessage;
import com.sicnu.community.pojo.User;
import com.sicnu.community.service.login.UserService;
import com.sicnu.community.util.JsonUtils;
import com.sicnu.community.util.RandomUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Slf4j
@Service
public class EmailUserService extends UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private RedisTemplate<String, Serializable> redisTemplate;

    @Value("${expireTime.verifyCode}")
    private String verifyCodeExpireTime;

    @Resource
    private RandomUtil randomUtil;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private ApolloConfig apolloConfig;

    @Override
    public User loadUser(String email) {
        return userDao.findUserByEmail(email);
    }

    @Override
    protected boolean isRegistered(String email) {
        // 检查邮箱是否已注册
        User user = userDao.findUserByEmail(email);
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean checkVerifyCode(String email, String verifyCode) {
        // 检验验证码
        String codeInRedis = (String) redisTemplate.opsForValue().get(email);
        if (codeInRedis == null || !verifyCode.equals(codeInRedis)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected User createUser(String email, String passwordHash) {
        // 创建新用户
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPhone("");
        if (email.endsWith("@stu.sicnu.edu.cn") || email.endsWith("@sicnu.edu.cn")) {
            // 通过校园邮箱注册直接认证成功
            newUser.setSchoolEmail(email);
        } else {
            newUser.setSchoolEmail("");
        }
        newUser.setPassword(passwordHash);
        // 设置默认昵称 todo
        return newUser;
    }

    @Override
    public BackFrontMessage sendVerifyCode(String email, String operation) {
        // 获取apollo对应操作的配置信息
        Map<String, OperationDetail> operationMap = apolloConfig.getOperationMap();
        OperationDetail operationDetail = operationMap.get(operation);

        if (operationDetail != null) {
            MailMessage mailMessage = new MailMessage();
            mailMessage.setTo(new String[] { email });
            mailMessage.setTemplateId(operationDetail.getTemplateId());
            mailMessage.setSubject(operationDetail.getSubject());
            Map<String, String> params = new HashMap<>(1);
            String code = randomUtil.generateRandomCode(6);
            params.put("code", code);
            // 放入redis
            redisTemplate.opsForValue().set(email, code, Duration.parse(verifyCodeExpireTime));
            mailMessage.setParams(params);
            kafkaTemplate.send("mail", JsonUtils.toJsonString(mailMessage));
            log.info("[op:sendVerifyCode] email={} code={}", email, code);
            return new BackFrontMessage(200, "发送成功", null);
        } else {
            // 非法
            return null;
        }
    }

    @Override
    public String getLoginType() {
        return "email";
    }
}
