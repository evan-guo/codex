/**
 *
 */
package com.codex.security.authentication;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 授权信息管理器
 * 用于收集系统中所有 AuthorizeConfigProvider 并加载其配置
 *
 * @author evan guo
 * @since 1.0.0
 */
public interface AuthorizeConfigManager {

    void config(HttpSecurity http);

}
