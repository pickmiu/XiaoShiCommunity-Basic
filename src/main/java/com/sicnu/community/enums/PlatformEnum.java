package com.sicnu.community.enums;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
public enum PlatformEnum {
    TREEHOLE("treehole");

    private String value;

    PlatformEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
