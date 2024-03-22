package com.codex.security.captcha.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author guowei
 * @since 2023-01-13
 * 默认的短信验证码发送器
 */
@Slf4j
@Component
public class DefaultSmsCodeSender implements SmsCaptchaSender {

    @Override
    public void send(String mobile, String code) {
        log.warn("请配置真实的短信验证码发送器(SmsCaptchaSender)");
        log.info("向手机" + mobile + "发送短信验证码" + code);
    }

}
