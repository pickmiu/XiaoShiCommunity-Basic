package com.sicnu.community;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import com.sicnu.community.config.ApolloConfig;
import com.sicnu.community.dao.UserDao;
import com.sicnu.community.enums.LoginTypeEnum;
import com.sicnu.community.json.OperationDetail;
import com.sicnu.community.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.util.DigestUtils;

import static com.sicnu.community.enums.PlatformEnum.TREEHOLE;

@SpringBootTest
class XiaoShiCommunityBasicApplicationTests {

    @Resource
    private RedisTemplate<String, Serializable> redisTemplate;

    @Resource
    private EmailUtil emailUtil;

    @Resource
    private Random random;

    @Resource
    private ApolloConfig apolloConfig;

    @Resource
    private RandomUtil randomUtil;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private CacheTest cacheTest;

    @Resource
    private UserDao userDao;

    @Test
    void contextLoads() throws Exception {
        //        String key = "user";
        //        User user1 = new User();
        //        user1.setId(1);
        //        redisTemplate.opsForValue().set(key, user1);
        //        User user2 = (User) redisTemplate.opsForValue().get(key);
        //        System.out.println(user2);

        //          boolean flag = true;
        //          int count = 0;
        //          for (int i = 0; i < 10000; i++) {
        //              if (ByteUtils.bytes2Int(ByteUtils.int2Byte(i)) != i) {
        //                  flag = false;
        //                  count++;
        //              }
        //          }
        //          if (flag) {
        //              System.out.println("success");
        //          } else {
        //              System.out.println("fail "+count);
        //          }

        //        int conflictCount = 0;
        //        HashSet<String> hashSet =new HashSet<>(100000);
        //        long startTime = System.currentTimeMillis();
        //        for (int i = 0; i < 1000000; i++) {
        //            String code = DigestUtils.md5DigestAsHex(ByteUtils.int2Byte(random.nextInt()));
        ////            if (!hashSet.contains(code)) {
        ////                hashSet.add(code);
        ////            } else {
        ////                conflictCount++;
        ////            }
        //        }
        //        long endTime = System.currentTimeMillis();
        //        System.out.println(endTime - startTime);
        //        int fail = 0;
        //        long start = System.currentTimeMillis();
        //        HashSet<String> hashSet = new HashSet<>(1000000);
        //        for (int i = 0; i < 1000000; i++) {
        //            String hash = EncryptUtil.hmacSHA1EncryptBase64(((Integer)i).toString(), "hello");
        //            if (hashSet.contains(hash)) {
        //                fail++;
        //            } else {
        //                hashSet.add(hash);
        //            }
        //        }
        //        long end = System.currentTimeMillis();
        //        System.out.println("fail "+ fail + ", "+(end-start));
        //        for (int i = 0; i < 100; i++) {
        //            String a = EncryptUtil.hmacSHA1EncryptBase64(i + "", "key");
        //            System.out.println(a);
        //        }
        //        System.out.println(PlatformEnum.TREEHOLE);
//        emailUtil.sendWarnEmail(
//            "com.sicnu.community.XiaoShiCommunityBasicApplicationTests.contextLoads.([]) line:80 info:测试邮件预警内容 author:pickmiu");


//        cacheTest.addUser(new CacheTest.User(1, "XM"));
//        System.out.println(cacheTest.getUser(1));
//        cacheTest.updateUser(new CacheTest.User(1, "123"));
//        System.out.println(cacheTest.getUser(1));
        Map<String, OperationDetail> map = new HashMap<>();
        map.put("register", new OperationDetail("register", "新用户注册", "1"));
        map.put("newPassword", new OperationDetail("newPassword", "重置密码", "1"));
        map.put("shoolEmailVerify", new OperationDetail("shoolEmailVerify", "校园邮箱认证", "3"));
        System.out.println(JsonUtils.toJsonString(map));
    }

    void testCacheDao() {

    }

}
