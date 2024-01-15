package com.codex.core.exception;

/**
 * 数据库类型不支持异常
 *
 * @author evan guo
 */
public class DatabaseTypeNotSupportException extends RuntimeException {

    public DatabaseTypeNotSupportException(String message) {
        super(message);
    }

    public DatabaseTypeNotSupportException(String message, Throwable cause) {
        super(message, cause);
    }

}
