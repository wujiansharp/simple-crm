package com.crm.util;
import org.springframework.util.DigestUtils;

public class Md5Util {

    // 简单盐值，自行修改
    private static final String SALT = "crm@2026";

    /**
     * MD5加盐加密
     */
    public static String encrypt(String pwd) {
        String src = pwd + SALT;
        return DigestUtils.md5DigestAsHex(src.getBytes());
    }

    /**
     * 密码校验
     */
    public static boolean match(String rawPwd, String dbPwd) {
        String md5Str=encrypt(rawPwd);
        return md5Str.equals(dbPwd);
    }
}