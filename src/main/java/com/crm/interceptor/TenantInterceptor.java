package com.crm.interceptor;

import com.crm.util.JwtUtil;
import com.crm.util.TenantContext;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    // 排除登录接口
    private static final List<String> WHITE_LIST = Arrays.asList("/sys/user/login");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();
        if(WHITE_LIST.contains(uri)){
            return true;
        }
        String token = request.getHeader("token");
        if(token == null){
            throw new RuntimeException("未登录");
        }
        // 解析token拿到 merchantId、userId
        Claims claims = JwtUtil.getClaimsByToken(token);
        Long merchantId = Long.valueOf(claims.get("merchantId").toString());
        TenantContext.setMerchantId(merchantId);
        TenantContext.setToken(token);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContext.clear();
    }
}
