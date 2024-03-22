package com.codex.security.sms;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author guowei
 * @since 2023-01-16
 * 短信登录过滤器
 */
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    protected SmsCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher("/oauth/login/phone", HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!request.getMethod().equals(HttpMethod.POST.name())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = ServletRequestUtils.getStringParameter(request, "username", "");
        SmsCodeAuthenticationToken authenticationToken = new SmsCodeAuthenticationToken(username);
        authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }


}
