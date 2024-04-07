package com.codex.security.properties;

/**
 * @author evan guo
 * @since 2023-01-13
 * Security的缓存相关常量
 */
public class SecurityCacheKey {

    /**
     * 登录Token
     */
    public static final String OAUTH_TOKEN = "oauth:token:";
    /**
     * 登录用户
     */
    public static final String OAUTH_USER = "oauth:user:";
    /**
     * 验证码
     */
    public static final String SECURITY_CAPTCHA = "oauth:captcha:";

}
