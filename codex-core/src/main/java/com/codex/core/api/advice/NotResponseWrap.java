package com.codex.core.api.advice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gw
 * 有时候第三方接口回调我们的接口，我们的接口必须按照第三方定义的返回格式来，
 * 此时第三方不一定和我们自己的返回格式一样，所以要提供一种可以绕过统一包装的方式。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface NotResponseWrap {
}