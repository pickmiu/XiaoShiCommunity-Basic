package com.sicnu.community.util;

import javax.servlet.http.HttpServletResponse;

import com.sicnu.community.json.BackFrontMessage;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Slf4j
public class ResponseUtil {

    public static void writeJson(HttpServletResponse response, Object data) {
        response.setContentType("application/json;charset=utf-8");
        try {
            response.getWriter().write(JsonUtils.toJsonString(data));
            response.flushBuffer();
        } catch (Exception e) {
            log.error("[op:writeJson] catch-exception response={} data={}", response, data, e);
        }
    }
}
