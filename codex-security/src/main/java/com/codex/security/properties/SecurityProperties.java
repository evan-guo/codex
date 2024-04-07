package com.codex.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author evan guo
 * @since 2023-01-12
 */
@Data
@Component
@ConfigurationProperties(prefix = "codex.security")
public class SecurityProperties {

    /**
     * 登录Token配置
     */
    private TokenProperties token;

    /**
     * 登录验证码配置
     */
    private SecurityCaptchaProperties captcha;

}
