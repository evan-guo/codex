package com.codex.security.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author evan guo
 * @since 2023-01-12
 * 登录验证码参数配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "codex.security.captcha")
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
    private EmailCaptchaProperties email = new EmailCaptchaProperties();

    @Data
    public static class SmsCaptchaProperties {
        /**
         * 是否开启
         */
        private Boolean enable = false;
        /**
         * 验证码长度
         */
        private int length = 6;
        /**
         * 验证码有效期，默认5分钟
         */
        private Duration duration = Duration.ofMinutes(5);
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
    public static class EmailCaptchaProperties extends SmsCaptchaProperties {
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
        private String subject = "验证码";
        /**
         * 使用SSL安全连接
         */
        private Boolean sslEnable = false;
    }

}
