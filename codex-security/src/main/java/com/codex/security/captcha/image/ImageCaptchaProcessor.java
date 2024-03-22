package com.codex.security.captcha.image;

import com.codex.security.captcha.AbstractCaptchaProcessor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

/**
 * @author guowei
 * @since 2023-01-13
 * 图片验证码处理器
 */
@RequiredArgsConstructor
@Component("imageCaptchaProcessor")
public class ImageCaptchaProcessor extends AbstractCaptchaProcessor<ImageCaptcha> {

    private final HttpServletResponse response;

    @SneakyThrows
    @Override
    protected void send(String account, ImageCaptcha captcha) {
        ImageIO.write(captcha.getImage(), "JPEG", response.getOutputStream());
    }

}
