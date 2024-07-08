package com.codex.security.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

/**
 * 默认UserDetailService实现类
 *
 * @author wei.guo
 */
@Service
public class DefaultUserDetailServiceImpl extends InMemoryUserDetailsManager {



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userDetails;
    }

}
