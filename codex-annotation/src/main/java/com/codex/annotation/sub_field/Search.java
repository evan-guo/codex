package com.codex.annotation.sub_field;

import com.codex.annotation.config.Comment;

/**
 * 使用在EntityField上，表示允许该字段作为查询条件
 *
 * @author evanguo
 * @since 1.0.0
 */
public @interface Search {

    boolean value() default true;

    @Comment("查询类型")
    SearchType type() default SearchType.EQ;

}
