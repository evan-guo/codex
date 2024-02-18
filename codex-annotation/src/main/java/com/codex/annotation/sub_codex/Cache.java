package com.codex.annotation.sub_codex;

import com.codex.annotation.config.Comment;

/**
 * 缓存注解，在{@link com.codex.annotation.Codex} 中使用，用来开启数据缓存
 *
 * @author evanguo
 * @since 1.0.0
 */
public @interface Cache {

    @Comment("启用缓存")
    boolean enable() default false;

    @Comment("缓存key")
    String key() default "";

}
