package com.sicnu.community.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.sicnu.community.config.ApolloConfig;
import com.sicnu.community.json.MailMessage;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Component
public class EmailUtil {

    @Resource
    private ApolloConfig apolloConfig;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendWarnEmail(String msg) {
        Map<String, String> params = new HashMap<>(1);
        params.put("msg", msg);
        List<String> developmentEmailList = apolloConfig.getDevelopmentEmailList();
        MailMessage mailMessage = new MailMessage(developmentEmailList.toArray(new String[developmentEmailList.size()]),
            "2", "系统预警", params);
        kafkaTemplate.send("mail", JsonUtils.toJsonString(mailMessage));
    }
}
