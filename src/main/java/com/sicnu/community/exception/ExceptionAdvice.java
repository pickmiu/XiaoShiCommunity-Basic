package com.sicnu.community.exception;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sicnu.community.json.BackFrontMessage;
import com.sicnu.community.util.EmailUtil;
import com.sicnu.community.util.ValidationUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 生产环境下启用
 * 
 * @author Tangliyi (2238192070@qq.com)
 */
//@Profile("online")
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @Resource
    private EmailUtil emailUtil;

    @ExceptionHandler(value = ValidationUtil.IllegalParamsException.class)
    public BackFrontMessage handleException(ValidationUtil.IllegalParamsException e) {
        return new BackFrontMessage(500, "参数错误:" + e.getMessage(), null);
    }

    @ExceptionHandler(value = ServiceExcption.class)
    public BackFrontMessage handleException(ServiceExcption e) {
        emailUtil.sendWarnEmail(
            "com.sicnu.community.exception.ControllerExceptionAdvice.handleException.([com.sicnu.community.exception.ServiceExcption]) line:26 info:"
                + e.getMessage() + " author:pickmiu");
        return new BackFrontMessage(500, "系统异常，请重试", null);
    }
}
