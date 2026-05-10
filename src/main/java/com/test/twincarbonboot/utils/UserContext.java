package com.test.twincarbonboot.utils;

/**
 * 基于 ThreadLocal 的用户上下文
 * 在同一次请求线程内，任何位置都能获取当前登录用户信息
 */
public class UserContext {

    private static final ThreadLocal<String> USERNAME_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<Integer> USER_ID_HOLDER = new ThreadLocal<>();

    /**
     * 设置当前登录用户名
     */
    public static void setUser(String username) {
        USERNAME_HOLDER.set(username);
    }

    /**
     * 获取当前登录用户名
     */
    public static String getUser() {
        return USERNAME_HOLDER.get();
    }

    /**
     * 设置当前登录用户ID
     */
    public static void setUserId(Integer userId) {
        USER_ID_HOLDER.set(userId);
    }

    /**
     * 获取当前登录用户ID
     */
    public static Integer getUserId() {
        return USER_ID_HOLDER.get();
    }

    /**
     * 清理当前线程的用户信息（防止线程池复用导致数据泄露）
     */
    public static void clear() {
        USERNAME_HOLDER.remove();
        USER_ID_HOLDER.remove();
    }
}
