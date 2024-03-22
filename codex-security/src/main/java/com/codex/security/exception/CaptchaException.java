package com.codex.security.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 验证码异常
 *
 * @author evan guo
 */
@Setter
@Getter
public class CaptchaException extends RuntimeException {

    public CaptchaException(String message) {
        super(message);
    }

}
