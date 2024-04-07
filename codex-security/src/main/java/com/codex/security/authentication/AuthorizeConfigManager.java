/**
 * 
 */
package com.codex.security.authentication;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * 授权信息管理器
 * 
 * 用于收集系统中所有 AuthorizeConfigProvider 并加载其配置
 * 
 * @author zhailiang
 *
 */

/**
 * @author evan guo
 * @since 2024-03-28
 * 授权信息管理器
 * 用于收集系统中所有 AuthorizeConfigProvider 并加载其配置
 */
public interface AuthorizeConfigManager {

	void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);

}
