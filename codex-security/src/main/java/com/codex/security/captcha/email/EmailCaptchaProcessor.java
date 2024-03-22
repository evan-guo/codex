package com.codex.security.captcha.email;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.codex.security.captcha.AbstractCaptchaProcessor;
import com.codex.security.captcha.Captcha;
import com.codex.security.properties.SecurityCaptchaProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * @author guowei
 * @since 2023-01-14
 * 邮箱验证码处理器
 */
@RequiredArgsConstructor
@Component("emailCaptchaProcessor")
public class EmailCaptchaProcessor extends AbstractCaptchaProcessor<Captcha> {

    private final SecurityCaptchaProperties captchaProperties;

    @SneakyThrows
    @Override
    protected void send(String account, Captcha validateCode) {
        MailAccount mailAccount = new MailAccount();
        mailAccount.setHost(captchaProperties.getEmail().getHost());
        mailAccount.setPort(captchaProperties.getEmail().getPort());
        mailAccount.setSslProtocols(captchaProperties.getEmail().getProtocol());
        MailUtil.send(mailAccount, account, "验证码", validateCode.getCode(), false);
    }

}