package com.crm.interceptor;

import com.crm.entity.SysUser;
import com.crm.util.JwtUtil;
import com.crm.util.LoginUserContext;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器：把用户信息塞进上下文
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. 获取 token
        String token = request.getHeader("token");

        // 2. 如果没有 token，直接放行（不塞用户信息）
        if (token == null || token.trim().isEmpty()) {
            return true;
        }

        try {
            // 3. 有 token 才解析
            Claims claims = JwtUtil.parseToken(token);

            SysUser user = new SysUser();
            user.setId(claims.get("userId", Long.class));
            user.setMerchantId(claims.get("merchantId", Long.class));
            user.setRoleType(claims.get("roleType", Integer.class));

            LoginUserContext.setLoginUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求结束必须清除！防止内存泄漏
        LoginUserContext.clear();
    }
}
