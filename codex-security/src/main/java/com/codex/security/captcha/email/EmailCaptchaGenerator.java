package com.codex.security.captcha.email;

import cn.hutool.core.util.RandomUtil;
import com.codex.security.captcha.Captcha;
import com.codex.security.captcha.CaptchaGenerator;
import com.codex.security.exception.CaptchaException;
import com.codex.security.properties.SecurityCaptchaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 邮箱验证码生成器
 *
 * @author evan guo
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Component("emailCaptchaGenerator")
public class EmailCaptchaGenerator implements CaptchaGenerator {
    private final SecurityCaptchaProperties captchaProperties;

    @Override
    public Captcha generate() {
        if (!captchaProperties.getEmail().getEnable()) {
            throw new CaptchaException("未启用邮箱验证码功能, 请配置codex.security.captcha.email.enable=true");
        }
        String code = RandomUtil.randomNumbers(captchaProperties.getEmail().getLength());
        return new Captcha(code, captchaProperties.getEmail().getDuration());
    }

}
