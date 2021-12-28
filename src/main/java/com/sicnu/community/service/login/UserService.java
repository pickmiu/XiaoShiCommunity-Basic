package com.sicnu.community.service.login;

import static com.sicnu.community.util.UserAuthenticationUtil.getCurrentUser;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import com.sicnu.community.config.ApolloConfig;
import com.sicnu.community.json.OperationDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;

import com.sicnu.community.dao.UserDao;
import com.sicnu.community.enums.PlatformEnum;
import com.sicnu.community.exception.ServiceExcption;
import com.sicnu.community.json.BackFrontMessage;
import com.sicnu.community.json.MailMessage;
import com.sicnu.community.pojo.User;
import com.sicnu.community.util.*;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Slf4j
public abstract class UserService {

    @Value("${encrypt.secret-key.treehole}")
    private String treeholeSecretKey;

    @Resource
    private EmailUtil emailUtil;

    @Value("${encrypt.secret-key.database}")
    private String databaseSecretKey;

    @Value("${expireTime.verifyCode}")
    private String verifyCodeExpireTime;

    @Value("${hostAddress}")
    private String hostAddress;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private RedisTemplate<String, Serializable> redisTemplate;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private Random random;

    @Resource
    private UserDao userDao;

    @Value("${expireTime.authCode}")
    private String authCodeExpireTime;

    @Resource
    private ApolloConfig apolloConfig;

    public BackFrontMessage getUserDetail(String code) {
        String result = (String) redisTemplate.opsForValue().get(code);
        if (result == null) {
            return null;
        } else {
            String[] args = result.split("\\|");
            if ("authCode".equals(args[0])) {
                if (PlatformEnum.TREEHOLE.getValue().equals(args[1])) {
                    String id = args[2];
                    if (!ValidationUtil.notNullAndEmptyBoolean(id)) {
                        log.warn("[op:getUserDetail] id为空");
                        return null;
                    }
                    String uniqueKey = null;
                    try {
                        uniqueKey = EncryptUtil.hmacSHA1EncryptBase64(id, treeholeSecretKey);
                    } catch (Exception e) {
                        log.error("[op:getUserDetail] catch-exception code={}", code, e);
                        emailUtil.sendWarnEmail(
                            "com.sicnu.community.service.auth.impl.UserServiceImpl.getUserDetail.([java.lang.String]) line:64 info:{树洞用户id散列生成异常} author:pickmiu");
                    }
                    return new BackFrontMessage(200, "获取成功", uniqueKey);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public abstract User loadUser(String username);


    public User loadUserByUserId(int userId) {
        return userDao.findUserById(userId);
    }

    public User login(String username, String password) {
        User user = loadUser(username);
        if (user == null || !checkPassword(password, user.getPassword())) {
            return null;
        }
        return user;
    }

    private boolean checkPassword(String password, String passwordHash) {
        String encryptPassword = null;
        try {
            encryptPassword = EncryptUtil.hmacSHA1EncryptBase64(password, databaseSecretKey);
        } catch (Exception e) {
            log.error("[op:checkPassword] catch-exception 密码加密发生异常", e);
            emailUtil.sendWarnEmail(
                "com.sicnu.community.service.login.UserService.checkPassword.([java.lang.String, java.lang.String]) line:82 info:密码加密发生异常 author:pickmiu");
            return false;
        }
        if (encryptPassword.equals(passwordHash)) {
            return true;
        } else {
            return false;
        }
    }

    public BackFrontMessage getAuthorizationCode(String platform) {
        User user = getCurrentUser();

        if (PlatformEnum.TREEHOLE.getValue().equals(platform) && user.getSchoolEmail() == null) {
            // 树洞平台授权需要实名认证
            return new BackFrontMessage(400, "需要学生认证", null);
        }

        // 创建code,并将code放入redis
        String code = redisUtil.setRandomCodeAsKey("authCode|" + platform + "|" + user.getId(),
            Duration.parse(authCodeExpireTime));
        log.info("[op:getAuthorizationCode] code={}", code);
        return new BackFrontMessage(200, "获取成功", code);
    }

    protected abstract boolean isRegistered(String username);

    protected abstract boolean checkVerifyCode(String username, String verifyCode);

    protected abstract User createUser(String username, String passwordHash);

    public BackFrontMessage register(String username, String password, String verifyCode) throws ServiceExcption {

        // 确认是否已注册
        if (isRegistered(username)) {
            return new BackFrontMessage(500, "账号已注册", null);
        }

        // 验证 验证码
        if (!checkVerifyCode(username, verifyCode)) {
            return new BackFrontMessage(500, "验证码错误", null);
        }

        // 密码取散列
        String passwordHash = EncryptUtil.hmacSHA1EncryptBase64(password, databaseSecretKey);
        if (passwordHash == null) {
            throw new ServiceExcption("密码加密发生异常");
        }

        User newUser = createUser(username, passwordHash);

        // 存入数据库
        if (userDao.addUser(newUser) != null) {
            // 成功
            // 可以发送欢迎邮件 恭喜用户注册成功 平台介绍 todo
            log.info("[op:register] newUser={} register success", newUser.toString());
            return new BackFrontMessage(200, "注册成功", null);
        } else {
            return new BackFrontMessage(500, "系统错误,请重试", null);
        }
    }

    public BackFrontMessage changePassword(String username, String newPassword, String verifyCode)
        throws ServiceExcption {
        if (!checkVerifyCode(username, verifyCode)) {
            return new BackFrontMessage(500, "验证码错误", null);
        }

        User user = loadUser(username);
        if (user == null) {
            return new BackFrontMessage(500, "用户不存在", null);
        }

        // 密码取散列
        String passwordHash = EncryptUtil.hmacSHA1EncryptBase64(newPassword, databaseSecretKey);
        if (passwordHash == null) {
            throw new ServiceExcption("密码加密发生异常");
        }
        user.setPassword(passwordHash);
        user.setPasswordChangeTime(new Date());

        if (userDao.updateUser(user) != null) {
            return new BackFrontMessage(200, "修改成功", null);
        } else {
            return new BackFrontMessage(500, "修改失败", null);
        }

    }

    public BackFrontMessage sendVerifyEmail(String email) {
        // 获取apollo对应操作的配置信息
        Map<String, OperationDetail> operationMap = apolloConfig.getOperationMap();
        OperationDetail operationDetail = operationMap.get("schoolEmailVerify");

        User currentUser = getCurrentUser();
        // 生成一段随机串
        String code = redisUtil.setRandomCodeAsKey("verifyEmail|" + currentUser.getId() + "|" + email,
            Duration.parse(verifyCodeExpireTime));
        // 构造消息
        MailMessage mailMessage = new MailMessage();
        mailMessage.setTo(new String[] { email });
        mailMessage.setTemplateId(operationDetail.getTemplateId());
        mailMessage.setSubject(operationDetail.getSubject());
        Map<String, String> params = new HashMap(1);
        // 构造确认链接
        String url = hostAddress + "/anon/validation?code=" + code;
        params.put("url", url);
        mailMessage.setParams(params);
        kafkaTemplate.send("mail", JsonUtils.toJsonString(mailMessage));
        log.info("[op:sendVerifyEmail] url={}", url);
        return new BackFrontMessage(200, "发送成功", null);
    }

    public BackFrontMessage validation(String code) throws ServiceExcption {
        String value = (String) redisTemplate.opsForValue().get(code);
        String args[] = value.split("\\|");
        if ("verifyEmail".equals(args[0])) {
            User user = new User();
            user.setId(Integer.parseInt(args[1]));
            user.setSchoolEmail(args[2]);
            if (userDao.updateUser(user) != null) {
                return new BackFrontMessage(200, "认证成功", null);
            } else {
                return new BackFrontMessage(500, "认证失败", null);
            }
        } else {
            return null;
        }
    }

    public abstract BackFrontMessage sendVerifyCode(String targetAddress, String operation);

    /**
     * 提供UserService实现处理的登录类型 例：“email” “phone”
     * 
     * @return
     */
    protected abstract String getLoginType();
}
