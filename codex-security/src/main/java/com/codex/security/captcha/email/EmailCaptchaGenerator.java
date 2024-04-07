package com.codex.security.captcha.email;

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
 * @since 2023-01-14
 * 邮箱验证码生成器
 */
@RequiredArgsConstructor
@Component("emailCaptchaGenerator")
public class EmailCaptchaGenerator implements CaptchaGenerator {
    private final SecurityCaptchaProperties captchaProperties;

    @Override
    public Captcha generate() {
        if (!captchaProperties.getEmail().getEnable()) {
            throw new CaptchaException("未开启邮箱验证码功能");
        }
        String code = RandomUtil.randomNumbers(captchaProperties.getEmail().getLength());
        return new Captcha(code, captchaProperties.getEmail().getDuration());
    }

}
