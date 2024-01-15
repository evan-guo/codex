package com.codex.annotation;

import java.lang.annotation.*;

/**
 * @author evanguo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface CodexField {

    @Comment("字段名称")
    String title();

    @Comment("字段描述")
    String desc() default "";

    @Comment("是否必填")
    boolean notNull() default false;

    @Comment("是否允许以该字段进行查询")
    Search search() default @Search(false);

    @Comment("是否导出")
    boolean export() default true;

}
