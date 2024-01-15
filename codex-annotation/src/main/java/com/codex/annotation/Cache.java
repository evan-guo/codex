package com.codex.annotation;

/**
 * @author evanguo
 */
public @interface Cache {

    @Comment("启用缓存")
    boolean enable() default false;

    @Comment("缓存key")
    String key() default "";

}
