package com.codex.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author evanguo
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD, ElementType.TYPE_PARAMETER, ElementType.PARAMETER})
@Documented
public @interface Comment {
    String value();

}
