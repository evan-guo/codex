package com.codex.security.exception;

/**
 * 权限校验异常
 *
 * @author evan guo
 */
public class SecurityException extends RuntimeException {

    public SecurityException(String message) {
        super(message);
    }

}
