package com.sicnu.community.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 合法性验证工具类
 * 
 * @author Tangliyi (2238192070@qq.com)
 */
public class ValidationUtil {

    private static final Pattern emailpattern = Pattern
        .compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

    /**
     * 不合法抛出的异常
     */
    public static class IllegalParamsException extends Exception {
        public IllegalParamsException(String message) {
            super(message);
        }
    }

    public static void email(String email) throws IllegalParamsException {
        notNullAndEmpty(email);
        Matcher m = emailpattern.matcher(email);
        if (!m.matches()) {
            throw new IllegalParamsException("email illegal");
        }
    }

    public static void notNullAndEmpty(String str) throws IllegalParamsException {
        notNull(str);
        if ("".equals(str)) {
            throw new IllegalParamsException("empty");
        }
    }

    public static boolean notNullAndEmptyBoolean(String str) {
        if (str == null || "".equals(str)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean notNullAndEmptyBoolean(String ...args) {
        for (String str : args) {
            if (!notNullAndEmptyBoolean(str)) {
                return false;
            }
        }
        return true;
    }

    public static void notNull(Object o) throws IllegalParamsException {
        if (o == null) {
            throw new IllegalParamsException("null");
        }
    }

    public static void password(String password) throws IllegalParamsException {
        notNullAndEmpty(password);
        length(password, 8, 100);
    }

    public static void length(String str, int min, int max) throws IllegalParamsException {
        notNull(str);
        if (str.length() < min || str.length() > max) {
            throw new IllegalParamsException("length illegal");
        }
    }

}
