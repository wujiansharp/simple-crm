package com.crm.util;

public class TenantContext {
    private static final ThreadLocal<Long> MERCHANT_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> TOKEN = new ThreadLocal<>();

    public static void setMerchantId(Long id){
        MERCHANT_ID.set(id);
    }
    public static Long getMerchantId(){
        return MERCHANT_ID.get();
    }

    public static void setToken(String token){
        TOKEN.set(token);
    }
    public static String getToken(){
        return TOKEN.get();
    }

    public static void clear(){
        MERCHANT_ID.remove();
        TOKEN.remove();
    }
}
