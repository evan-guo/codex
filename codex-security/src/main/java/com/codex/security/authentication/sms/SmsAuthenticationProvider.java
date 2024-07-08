package com.codex.security.authentication.sms;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * 短信登录验证逻辑，由于短信验证码的验证在过滤器里已完成，这里直接读取用户信息即可
 *
 * @author evan guo
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Component
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 根据手机号查询用户
        String username = (String) authentication.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new SecurityException("无法获取用户信息");
        }
        // 包装用户信息
        SmsAuthenticationToken authenticationToken = new SmsAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationToken.setDetails(authentication.getDetails());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
