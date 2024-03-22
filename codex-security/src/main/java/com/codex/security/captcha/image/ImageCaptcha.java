package com.codex.security.captcha.image;

import com.codex.security.captcha.Captcha;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author guowei
 * @since 2023-01-13
 * 图形验证码
 */
@Setter
@Getter
public class ImageCaptcha extends Captcha {

    private BufferedImage image;

    public ImageCaptcha(BufferedImage image, String code, Duration duration) {
        super(code, duration);
        this.image = image;
    }

    public ImageCaptcha(BufferedImage image, String code, LocalDateTime expireTime) {
        super(code, expireTime);
        this.image = image;
    }

}
