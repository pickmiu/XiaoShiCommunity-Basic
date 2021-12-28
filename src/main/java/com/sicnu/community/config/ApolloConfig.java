package com.sicnu.community.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ctrip.framework.apollo.Config;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sicnu.community.json.OperationDetail;
import com.sicnu.community.util.JsonUtils;

import lombok.Data;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Data
@Component
public class ApolloConfig {

    @Resource
    private Config config;

    /**
     * 平台开发者列表
     */
    @Value("#{'${platformDeveloperEmail}'.split(',')}")
    private List<String> developmentEmailList;

    @Value("${enableCache}")
    private boolean enableCache;

    // 技术需求 todo @Value注解序列化json的改造

    private Map<String, OperationDetail> operationMap;

    public Map<String, OperationDetail> getOperationMap() {
        return config.getProperty("mail.operation", str -> JsonUtils.parse(str, new JsonMapper().getTypeFactory()
            .constructParametricType(HashMap.class, String.class, OperationDetail.class)), new HashMap(0));
    }

}
