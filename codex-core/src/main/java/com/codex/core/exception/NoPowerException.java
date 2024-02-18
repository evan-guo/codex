package com.codex.core.exception;

/**
 * 未授予对应权限异常
 *
 * @author evan guo
 */
public class NoPowerException extends RuntimeException {

    public NoPowerException(String message) {
        super(message);
    }

}
