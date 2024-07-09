package com.codex.security.captcha.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 默认的短信验证码发送器
 *
 * @author evan guo
 * @since 1.0.0
 */
@Slf4j
@Component
public class DefaultSmsCodeSender implements SmsCaptchaSender {

    @Override
    public void send(String mobile, String code) {
        log.warn("请配置真实的短信验证码发送器 (SmsCaptchaSender)");
        log.info("发送短信验证码成功，account：{}，code：{}", mobile, code);
    }

}
