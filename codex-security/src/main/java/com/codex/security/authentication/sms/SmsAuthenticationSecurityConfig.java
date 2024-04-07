package com.codex.security.authentication.sms;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.UUID;

/**
 * @author guo_wei
 * @date 2023-01-17
 * 短信登录配置
 */
@RequiredArgsConstructor
@Configuration
public class SmsAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    //private final UserDetailsService userDetailsService;
    // private final PersistentTokenRepository persistentTokenRepository;

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        SmsAuthenticationFilter smsAuthenticationFilter = new SmsAuthenticationFilter();
//        smsAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
//        // smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(imoocAuthenticationSuccessHandler);
//        // smsCodeAuthenticationFilter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);
//        String key = UUID.randomUUID().toString();
//        // smsCodeAuthenticationFilter.setRememberMeServices(new PersistentTokenBasedRememberMeServices(key, userDetailsService, persistentTokenRepository));
//        SmsAuthenticationProvider smsAuthenticationProvider = new SmsAuthenticationProvider();
//        smsAuthenticationProvider.setUserDetailsService(userDetailsService);
//        http.authenticationProvider(smsAuthenticationProvider).addFilterAfter(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
