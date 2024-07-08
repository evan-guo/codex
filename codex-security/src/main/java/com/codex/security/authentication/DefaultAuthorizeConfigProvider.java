/**
 * 
 */
package com.codex.security.authentication;

import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 核心模块的授权配置提供器，安全模块涉及的url的授权配置在这里。
 * 
 * @author zhailiang
 *
 */
@Component
@Order(Integer.MIN_VALUE)
public class DefaultAuthorizeConfigProvider implements AuthorizeConfigProvider {

	@Override
	public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
		config.requestMatchers("/oauth/*").permitAll();
		return false;
	}

}
