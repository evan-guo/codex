package com.codex.annotation;

import com.codex.annotation.config.Comment;
import com.codex.annotation.sub_codex.Cache;
import com.codex.annotation.sub_codex.DataProxy;
import com.codex.annotation.sub_codex.Power;

import java.lang.annotation.*;

/**
 * 核心注解，在Entity上使用该注解，用以开启Codex相关功能
 *
 * @author evan guo
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Codex {

    @Comment("功能名称")
    String name() default "";

    @Comment("功能描述")
    String desc() default "";

    @Comment("接口权限")
    Power power() default @Power;

    @Comment("数据缓存")
    Cache cache() default @Cache;

    @Comment("是否自动生成表")
    boolean ddlAuto() default true;

    @Comment("数据代理行为")
    Class<? extends DataProxy<?>>[] dataProxy() default {};

}
