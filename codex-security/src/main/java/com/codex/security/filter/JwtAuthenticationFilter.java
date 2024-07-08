package com.codex.security.filter;

import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.codex.security.properties.SecurityCacheKey;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT请求过滤器
 *
 * @author wei.guo
 */
@SuppressWarnings("NullableProblems")
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader("Authorization");
        if (!StringUtils.hasLength(authorization) || !StringUtils.startsWithIgnoreCase(authorization, "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String jwtToken = authorization.substring(7);
        JWT jwt = JWTUtil.parseToken(jwtToken);
        String username = (String) jwt.getPayload(JWTPayload.SUBJECT);
        // Redis查询Token是否存在, 如果不存在则解析JWT的过期时间
        Object object = redisTemplate.opsForValue().get(SecurityCacheKey.OAUTH_TOKEN + authorization);
        if (object == null) {
            DateTime expiresAt = (DateTime) jwt.getPayload(JWTPayload.EXPIRES_AT);
            if (expiresAt.before(DateTime.now())) {
                return;
            }
        }
        if (StringUtils.hasLength(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 查询用户
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // 对比密码
            if (username.equals(userDetails.getUsername())) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }
}
