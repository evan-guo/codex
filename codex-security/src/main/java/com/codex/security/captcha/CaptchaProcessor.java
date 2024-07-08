package com.codex.security.captcha;

/**
 * 验证码处理器，实现它以支持不同的验证码处理逻辑
 *
 * @author evan guo
 * @since 1.0.0
 */
public interface CaptchaProcessor {

    /**
     * 创建验证码
     * @param account 账号
     */
    void create(String account);

    /**
     * 校验验证码
     * @param account 账号
     * @param code    验证码
     */
    void validate(String account, String code);

}
