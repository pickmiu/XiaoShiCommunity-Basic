package com.sicnu.community.util;

import com.sicnu.community.pojo.User;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
public class UserAuthenticationUtil {
    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void setCurrentUser(User user) {
        threadLocal.set(user);
    }

    public static User getCurrentUser() {
        return threadLocal.get();
    }

    public static void removeUser() {
        threadLocal.remove();
    }

}