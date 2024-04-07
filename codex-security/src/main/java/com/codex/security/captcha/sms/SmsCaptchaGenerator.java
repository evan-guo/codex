package com.codex.security.captcha.sms;

import cn.hutool.core.util.RandomUtil;
import com.codex.security.captcha.Captcha;
import com.codex.security.captcha.CaptchaGenerator;
import com.codex.security.exception.CaptchaException;
import com.codex.security.exception.SecurityException;
import com.codex.security.properties.SecurityCaptchaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author evan guo
 * @since 2023-01-13
 * 短信验证码生成器
 */
@RequiredArgsConstructor
@Component
public class SmsCaptchaGenerator implements CaptchaGenerator {

    private final SecurityCaptchaProperties captchaProperties;

    @Override
    public Captcha generate() {
        if (!captchaProperties.getSms().getEnable()) {
            throw new CaptchaException("未开启短信验证码功能");
        }
        String code = RandomUtil.randomNumbers(captchaProperties.getSms().getLength());
        return new Captcha(code, captchaProperties.getSms().getDuration());
    }

}
