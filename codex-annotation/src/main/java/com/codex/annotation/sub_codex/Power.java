package com.codex.annotation.sub_codex;

import com.codex.annotation.config.Comment;

/**
 * 设置对应接口权限
 *
 * @author evanguo
 */
public @interface Power {

    @Comment("启用权限校验")
    boolean enable() default true;

    @Comment("新增权限")
    boolean add() default true;

    @Comment("修改权限")
    boolean edit() default true;

    @Comment("删除权限")
    boolean delete() default true;

    @Comment("查询权限")
    boolean query() default true;

    @Comment("导出权限")
    boolean export() default false;

    @Comment("导入权限")
    boolean importable() default false;


}
