package com.sicnu.community.enums;

/**
 * 登录方式（用户名类型）
 * @author Tangliyi (2238192070@qq.com)
 */
public enum LoginTypeEnum {
    DEFAULT("email"),
    EMAIL("email"),
    PHONE("phone");

    private String value;

    LoginTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}