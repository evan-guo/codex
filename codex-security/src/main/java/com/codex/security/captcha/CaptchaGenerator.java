package com.codex.security.captcha;

/**
 * @author guowei
 * @since 2023-01-13
 * 验证码生成器
 */
public interface CaptchaGenerator {

    /**
     * 生成验证码
     * @return 校验码
     */
    Captcha generate();

}
