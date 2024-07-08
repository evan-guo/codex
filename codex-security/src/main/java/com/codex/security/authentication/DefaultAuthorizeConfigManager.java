/**
 *
 */
package com.codex.security.authentication;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 默认的授权配置管理器
 *
 * @author zhailiang
 */
@RequiredArgsConstructor
@Component
public class DefaultAuthorizeConfigManager implements AuthorizeConfigManager {

    private final List<AuthorizeConfigProvider> authorizeConfigProviders;

    @SneakyThrows
    @Override
    public void config(HttpSecurity http) {
        http.authorizeHttpRequests(customizer -> {
            boolean existAnyRequestConfig = false;
            String existAnyRequestConfigName = null;
            for (AuthorizeConfigProvider authorizeConfigProvider : authorizeConfigProviders) {
                boolean currentIsAnyRequestConfig = authorizeConfigProvider.config(customizer);
                if (existAnyRequestConfig && currentIsAnyRequestConfig) {
                    throw new RuntimeException("重复的anyRequest配置:" + existAnyRequestConfigName + "," + authorizeConfigProvider.getClass().getSimpleName());
                } else if (currentIsAnyRequestConfig) {
                    existAnyRequestConfig = true;
                    existAnyRequestConfigName = authorizeConfigProvider.getClass().getSimpleName();
                }
            }
            if (!existAnyRequestConfig) {
                customizer.anyRequest().authenticated();
            }
        });
    }
}
