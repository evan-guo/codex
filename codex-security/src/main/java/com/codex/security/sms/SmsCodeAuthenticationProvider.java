package com.codex.security.sms;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author guowei
 * @since 2023-01-16
 * 短信登录验证逻辑，由于短信验证码的验证在过滤器里已完成，这里直接读取用户信息即可
 */
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 根据手机号查询用户
        String username = (String) authentication.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new SecurityException("无法获取用户信息");
        }
        // 包装用户信息
        SmsCodeAuthenticationToken authenticationToken = new SmsCodeAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationToken.setDetails(authentication.getDetails());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
