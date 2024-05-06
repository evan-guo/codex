package com.codex.api.exception;

/**
 * Codex类未找到异常
 *
 * @author evan guo
 */
public class CodexNotFoundException extends RuntimeException {

    public CodexNotFoundException(String codexName) {
        super("Codex [" + codexName + "] not found. Make sure this class exists, and is annotated with @CodexApi.");
    }

}
