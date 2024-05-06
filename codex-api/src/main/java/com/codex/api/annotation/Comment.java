package com.codex.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 起注释作用，编译后注释依然可以保留
 *
 * @author evanguo
 * @since 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD, ElementType.TYPE_PARAMETER, ElementType.PARAMETER})
@Documented
public @interface Comment {

    String value();

}
