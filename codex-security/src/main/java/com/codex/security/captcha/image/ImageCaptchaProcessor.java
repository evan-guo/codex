package com.codex.security.captcha.image;

import com.codex.security.captcha.AbstractCaptchaProcessor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;

/**
 * 图片验证码处理器
 *
 * @author evan guo
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Component
public class ImageCaptchaProcessor extends AbstractCaptchaProcessor<ImageCaptcha> {

    private final HttpServletResponse response;

    @SneakyThrows
    @Override
    protected void send(String account, ImageCaptcha captcha) {
        ImageIO.write(captcha.getImage(), "JPEG", response.getOutputStream());
    }

}
