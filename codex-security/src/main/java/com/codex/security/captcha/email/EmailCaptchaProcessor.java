package com.codex.security.captcha.email;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.codex.security.captcha.AbstractCaptchaProcessor;
import com.codex.security.captcha.Captcha;
import com.codex.security.exception.CaptchaException;
import com.codex.security.exception.SecurityException;
import com.codex.security.properties.SecurityCaptchaProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * @author evan guo
 * @since 2023-01-14
 * 邮箱验证码处理器
 */
@RequiredArgsConstructor
@Component
public class EmailCaptchaProcessor extends AbstractCaptchaProcessor<Captcha> {

    private final SecurityCaptchaProperties captchaProperties;

    @SneakyThrows
    @Override
    protected void send(String account, Captcha validateCode) {
        MailAccount mailAccount = new MailAccount();
        if (StrUtil.hasEmpty(captchaProperties.getEmail().getAccount(), captchaProperties.getEmail().getPassword())) {
            throw new CaptchaException("邮箱验证码发送失败，请配置邮箱账号密码");
        }
        if (StrUtil.isNotBlank(captchaProperties.getEmail().getHost())) {
            mailAccount.setHost(captchaProperties.getEmail().getHost());
        }
        if (captchaProperties.getEmail().getPort() != null) {
            mailAccount.setPort(captchaProperties.getEmail().getPort());
        }
        if (StrUtil.isNotBlank(captchaProperties.getEmail().getProtocol())) {
            mailAccount.setSslProtocols(captchaProperties.getEmail().getProtocol());
        }
        mailAccount.setFrom(captchaProperties.getEmail().getAccount());
        mailAccount.setPass(captchaProperties.getEmail().getPassword());
        mailAccount.setSslEnable(captchaProperties.getEmail().getSslEnable());
        MailUtil.send(mailAccount, account, captchaProperties.getEmail().getSubject(), validateCode.getCode(), false);
    }

}