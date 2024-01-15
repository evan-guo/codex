package com.codex.annotation;

import java.lang.annotation.*;

/**
 * 如果想要使Entity拥有Codex对应功能，请先在Entity类上加入此注解
 *
 * @author evan guo
 * @since 1.0
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

    @Comment("自动生成表")
    boolean ddlAuto() default true;

    @Comment("数据源，不填就使用默认的数据源")
    String ds() default "";

}
