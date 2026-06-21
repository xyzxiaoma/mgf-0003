package com.campus.club.interceptor;

import com.campus.club.common.BusinessException;
import com.campus.club.utils.JwtUtils;
import com.campus.club.utils.UserContext;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            throw new BusinessException(401, "未登录，请先登录");
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (!jwtUtils.validateToken(token)) {
            throw new BusinessException(401, "登录已过期，请重新登录");
        }

        Claims claims = jwtUtils.parseToken(token);
        if (claims == null) {
            throw new BusinessException(401, "无效的token");
        }

        Object userIdObj = claims.get("userId");
        Long userId;
        if (userIdObj instanceof Number) {
            userId = ((Number) userIdObj).longValue();
        } else {
            userId = Long.valueOf(userIdObj.toString());
        }
        String username = claims.get("username").toString();
        String role = claims.get("role").toString();

        UserContext.setUserId(userId);
        UserContext.setUsername(username);
        UserContext.setRole(role);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }
}
