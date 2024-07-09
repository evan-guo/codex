package com.codex.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author evan guo
 * @since 2023-01-12
 */
@Data
@Component
@ConfigurationProperties(prefix = "codex.security")
public class CodexSecurityProperties {

    /**
     * 登录Token配置
     */
    private TokenProperties token = new TokenProperties();

    /**
     * 登录验证码配置
     */
    private SecurityCaptchaProperties captcha;


    @Data
    public static class TokenProperties {
        /**
         * 登录生成Token的密钥
         */
        private String secret;
        /**
         * 登录生成Token的有效期
         */
        private Duration duration = Duration.ofHours(2);
    }

}
