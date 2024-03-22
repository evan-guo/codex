package com.codex.security.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author guowei
 * @since 2023-01-16
 * 短信登录验证信息封装
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    public SmsCodeAuthenticationToken(String phone) {
        super(null);
        this.principal = phone;
        super.setAuthenticated(false);
    }

    public SmsCodeAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

}
