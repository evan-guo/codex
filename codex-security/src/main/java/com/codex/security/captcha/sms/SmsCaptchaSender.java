package com.codex.security.captcha.sms;

/**
 * @author guowei
 * @since 2023-01-13
 * 短信发送器，实现它以支持不同的短信供应商逻辑
 */
public interface SmsCaptchaSender {

    /**
     * 发送短信
     * @param phone 手机号
     * @param code  验证码
     */
    void send(String phone, String code);

}
