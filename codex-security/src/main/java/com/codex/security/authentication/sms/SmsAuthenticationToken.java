package com.codex.security.authentication.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 短信登录验证信息封装
 *
 * @author evan guo
 * @since 1.0.0
 */
public class SmsAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    public SmsAuthenticationToken(String phone) {
        super(null);
        this.principal = phone;
        super.setAuthenticated(false);
    }

    public SmsAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
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
