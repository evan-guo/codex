package com.codex.cache;


/**
 * 缓存注解，用来开启数据缓存
 *
 * @author evanguo
 * @since 1.0.0
 */
public @interface EnableCodexCache {

    /**
     * 启用缓存
     */
    boolean enable() default false;

    /**
     * 缓存key
     */
    String key() default "";

}
