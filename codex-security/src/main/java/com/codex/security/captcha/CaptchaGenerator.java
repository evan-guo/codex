package com.codex.security.captcha;

/**
 * 验证码生成器
 *
 * @author evan guo
 * @since 1.0.0
 */
public interface CaptchaGenerator {

    /**
     * 生成验证码
     * @return 校验码
     */
    Captcha generate();

}
