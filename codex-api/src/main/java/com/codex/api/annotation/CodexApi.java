package com.codex.api.annotation;

import com.codex.api.proxy.DataProxy;

import java.lang.annotation.*;

/**
 * 在Entity上使用该注解，可开启对应的通用接口
 *
 * @author evan guo
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface CodexApi {

    @Comment("功能名称")
    String name() default "";

    @Comment("功能描述")
    String desc() default "";

    @Comment("接口权限")
    Power power() default @Power;

    @Comment("数据代理行为")
    Class<? extends DataProxy<?>>[] dataProxy() default {};


}
