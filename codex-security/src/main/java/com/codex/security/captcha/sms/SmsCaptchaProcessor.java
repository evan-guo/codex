package com.codex.security.captcha.sms;

import com.codex.security.captcha.AbstractCaptchaProcessor;
import com.codex.security.captcha.Captcha;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * 短信验证码处理器
 *
 * @author guo_wei
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Component
public class SmsCaptchaProcessor extends AbstractCaptchaProcessor<Captcha> {

    private final SmsCaptchaSender smsCaptchaSender;

    @SneakyThrows
    @Override
    protected void send(String account, Captcha captcha) {
        this.smsCaptchaSender.send(account, captcha.getCode());
    }

}
