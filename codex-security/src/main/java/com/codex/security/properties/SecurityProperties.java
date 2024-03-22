package com.codex.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author guowei
 * @since 2023-01-12
 */
@Data
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    /**
     * 登录Token配置
     */
    private LoginTokenProperties token;

    /**
     * 登录验证码配置
     */
    private SecurityCaptchaProperties captcha;

}
