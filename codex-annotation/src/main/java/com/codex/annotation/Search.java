package com.codex.annotation;

/**
 * @author evanguo
 */
public @interface Search {

    boolean value() default true;

    @Comment("高级查询")
    boolean vague() default false;

    @Comment("是否必填")
    boolean notNull() default false;

}
