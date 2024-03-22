package com.codex.security.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author guowei
 * @since 2023-01-12
 * 登录验证码参数配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "security.captcha")
public class SecurityCaptchaProperties {
    /**
     * 短信验证码
     */
    private SmsCaptchaProperties sms = new SmsCaptchaProperties();
    /**
     * 图片验证码
     */
    private ImageCaptchaProperties image = new ImageCaptchaProperties();
    /**
     * 邮箱验证码
     */
    private EmailCodeProperties email = new EmailCodeProperties();

    @Data
    public static class SmsCaptchaProperties {
        /**
         * 验证码长度
         */
        private int length = 6;
        /**
         * 验证码有效期，默认5分钟
         */
        private Duration duration = Duration.ofSeconds(300);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ImageCaptchaProperties extends SmsCaptchaProperties {
        /**
         * 图形验证码宽度
         */
        private int width = 67;
        /**
         * 图形验证码高度
         */
        private int height = 23;

        public ImageCaptchaProperties() {
            setLength(4);
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class EmailCodeProperties extends SmsCaptchaProperties {
        /**
         * 邮箱账号
         */
        private String account;
        /**
         * 邮箱密码
         */
        private String password;
        /**
         * 邮件服务器
         */
        private String host;
        /**
         * 端口
         */
        private Integer port;
        /**
         * 协议
         */
        private String protocol;
        /**
         * 验证码邮件主题
         */
        private String subject;
    }

}
