package com.codex.annotation.sub_field;

import com.codex.annotation.config.Comment;

/**
 * 条件查询类型
 *
 * @author evan guo
 * @since 1.0.0
 */
public enum SearchType {
    /**
     * 等于
     */
    @Comment("等于")
    EQ(),
    /**
     * IN查询
     */
    @Comment("IN查询")
    IN(),
    /**
     * NOT IN查询
     */
    @Comment("NOT IN查询")
    NOT_IN(),
    /**
     * 不等于
     */
    @Comment("不等于")
    NE(),
    /**
     * 大于
     */
    @Comment("大于")
    GT(),
    /**
     * 大于等于
     */
    @Comment("大于等于")
    GTE(),
    /**
     * 小于
     */
    @Comment("小于")
    LT(),
    /**
     * 小于等于
     */
    @Comment("小于等于")
    LTE(),
    /**
     * 模糊查询
     */
    @Comment("模糊查询")
    LIKE(),
    /**
     * 不模糊查询
     */
    @Comment("不模糊查询")
    NOT_LIKE(),
    /**
     * 范围查询
     */
    @Comment("范围查询")
    BETWEEN(),
    /**
     * 不范围查询
     */
    @Comment("不在范围内")
    NOT_BETWEEN(),
    /**
     * IS NULL
     */
    @Comment("IS NULL")
    IS_NULL(),
    /**
     * NOT NULL
     */
    @Comment("NOT NULL")
    IS_NOT_NULL();

    SearchType() {

    }

}
