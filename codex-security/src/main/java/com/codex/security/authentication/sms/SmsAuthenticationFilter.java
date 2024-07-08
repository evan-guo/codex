package com.codex.security.authentication.sms;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.ServletRequestUtils;

import java.io.IOException;

/**
 * 短信登录过滤器
 *
 * @author evan guo
 * @since 1.0.0
 */
public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    protected SmsAuthenticationFilter() {
        super(new AntPathRequestMatcher("/oauth/login/phone", HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!request.getMethod().equals(HttpMethod.POST.name())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = ServletRequestUtils.getStringParameter(request, "username", "");
        SmsAuthenticationToken authenticationToken = new SmsAuthenticationToken(username.trim());
        authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

}
