package com.codex.security.captcha.sms;

import com.codex.security.captcha.AbstractCaptchaProcessor;
import com.codex.security.captcha.Captcha;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * @author guo_wei
 * @since 2023-01-13
 * 短信验证码处理器
 */
@RequiredArgsConstructor
@Component("smsCaptchaProcessor")
public class SmsCaptchaProcessor extends AbstractCaptchaProcessor<Captcha> {

    private final SmsCaptchaSender smsCaptchaSender;

    @SneakyThrows
    @Override
    protected void send(String account, Captcha captcha) {
        this.smsCaptchaSender.send(account, captcha.getCode());
    }

}
