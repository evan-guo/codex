package com.codex.security.captcha.sms;

import cn.hutool.core.util.RandomUtil;
import com.codex.security.captcha.Captcha;
import com.codex.security.captcha.CaptchaGenerator;
import com.codex.security.properties.SecurityCaptchaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author guowei
 * @since 2023-01-13
 * 短信验证码生成器
 */
@RequiredArgsConstructor
@Component("smsCaptchaGenerator")
public class SmsCaptchaGenerator implements CaptchaGenerator {

    private final SecurityCaptchaProperties captchaProperties;

    @Override
    public Captcha generate() {
        String code = String.valueOf(RandomUtil.randomInt(captchaProperties.getSms().getLength()));
        return new Captcha(code, captchaProperties.getSms().getDuration());
    }

}
