package com.codex.security.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.codex.security.model.DefaultUserDetail;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

/**
 * 默认UserDetailService实现类
 *
 * @author wei.guo
 */
@RequiredArgsConstructor
@Service
public class DefaultUserDetailServiceImpl extends InMemoryUserDetailsManager {

    private final SecurityProperties securityProperties;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityProperties.User user = securityProperties.getUser();
        if (user.getName().equals(username)) {
            return new DefaultUserDetail(username, user.getPassword());
        }
        return null;
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        SecurityProperties.User user = securityProperties.getUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}