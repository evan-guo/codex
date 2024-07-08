package com.codex.security.captcha.image;

import com.codex.security.captcha.Captcha;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 图形验证码
 *
 * @author evan guo
 * @since 1.0.0
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
