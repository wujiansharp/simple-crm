package com.crm.util;

import com.crm.entity.SysUser;

/**
 * 登录用户上下文（整个请求都能获取当前登录人）
 */
public class LoginUserContext {
    private static final ThreadLocal<SysUser> USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置当前登录用户
     */
    public static void setLoginUser(SysUser sysUser) {
        USER_THREAD_LOCAL.set(sysUser);
    }

    /**
     * 获取当前登录用户
     */
    public static SysUser getLoginUser() {
        return USER_THREAD_LOCAL.get();
    }

    /**
     * 获取当前用户ID
     */
    public static Long getUserId() {
        SysUser user = getLoginUser();
        return user == null ? null : user.getId();
    }

    /**
     * 获取当前商户ID
     */
    public static Long getMerchantId() {
        SysUser user = getLoginUser();
        return user == null ? null : user.getMerchantId();
    }

    /**
     * 获取用户类型
     */
    public static Integer getUserType() {
        SysUser user = getLoginUser();
        return user == null ? null : user.getRoleType();
    }

    /**
     * 是否管理员（超级管理员 / 商户管理员）
     */
    public static boolean isAdmin() {
        SysUser user = getLoginUser();
        if (user == null) {
            return false;
        }
        // 1=超级管理员 2=商户管理员
        return Integer.valueOf(1).equals(user.getRoleType());
    }

    /**
     * 清除上下文（必须！防止内存泄漏）
     */
    public static void clear() {
        USER_THREAD_LOCAL.remove();
    }
}