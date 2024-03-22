package com.codex.security.captcha;

import lombok.Data;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author guowei
 * @since 2023-01-13
 * 验证码抽象类
 */
@Data
public class Captcha implements Serializable {

    private static final long serialVersionUID = 1588203828504660915L;

    private String code;

    private LocalDateTime expireTime;

    public Captcha(String code, Duration duration) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(duration.getSeconds());
    }

    public Captcha(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }

}
