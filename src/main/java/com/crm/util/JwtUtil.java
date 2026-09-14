package com.crm.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
public class JwtUtil {

    // 自定义密钥（自行修改，保证安全性）
    private static final String SECRET_KEY = "CrmSaas2026SmallShopMultiTenantSecretKey666";
    // 过期时间：7天 private static final long EXPIRE_TIME = 604800000L;
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;
    /**
     * 生成密钥
     */
    private static SecretKey getSecretKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成Token
     * @param userId 用户ID
     * @param merchantId 商户ID
     * @return token
     */
    public static String generateToken(Long userId, Long merchantId,Integer roleType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("merchantId", merchantId);
        claims.put("roleType", roleType);

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + EXPIRE_TIME);

        return Jwts.builder()
                .claim("userId", userId)
                .claim("merchantId", merchantId)
                .claim("roleType", roleType)
                .setExpiration(expireDate)
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * 解析Token，获取载荷
     */
    public static Claims getClaimsByToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new RuntimeException("token不能为空");
        }
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("token失效或非法，请重新登录");
        }
    }
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    /**
     * 从token获取当前登录用户ID
     */
    public static Long getUserId(String token) {
        Claims claims = getClaimsByToken(token);
        return Long.valueOf(claims.get("userId").toString());
    }

    /**
     * 从token获取当前商户ID
     */
    public static Long getMerchantId(String token) {
        Claims claims = getClaimsByToken(token);
        return Long.valueOf(claims.get("merchantId").toString());
    }
}
