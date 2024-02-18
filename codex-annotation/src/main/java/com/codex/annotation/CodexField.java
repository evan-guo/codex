package com.codex.annotation;

import com.codex.annotation.config.Comment;
import com.codex.annotation.sub_field.Search;

import java.lang.annotation.*;

/**
 * 核心注解，在Entity Field上使用
 *
 * @author evanguo
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface CodexField {

    @Comment("字段名称")
    String name();

    @Comment("字段描述")
    String desc() default "";

    @Comment("是否必填")
    boolean notNull() default false;

    @Comment("是否允许作为查询条件")
    Search search() default @Search(false);

}
