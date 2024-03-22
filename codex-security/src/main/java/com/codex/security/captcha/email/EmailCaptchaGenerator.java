package com.codex.security.captcha.email;

import cn.hutool.core.util.RandomUtil;
import com.codex.security.captcha.Captcha;
import com.codex.security.captcha.CaptchaGenerator;
import com.codex.security.properties.SecurityCaptchaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author guowei
 * @since 2023-01-14
 * 邮箱验证码生成器
 */
@RequiredArgsConstructor
@Component("emailCaptchaGenerator")
public class EmailCaptchaGenerator implements CaptchaGenerator {
    private final SecurityCaptchaProperties captchaProperties;

    @Override
    public Captcha generate() {
        String code = String.valueOf(RandomUtil.randomInt(captchaProperties.getEmail().getLength()));
        return new Captcha(code, captchaProperties.getEmail().getDuration());
    }

}
