package com.codex.core.exception;

/**
 * Codex类未找到异常
 *
 * @author evan guo
 */
public class CodexNotFoundException extends RuntimeException {

    public CodexNotFoundException(String codexName) {
        super("Codex [" + codexName + "] not found");
    }

}
