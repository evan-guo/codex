package com.codex.core.proxy;

import com.codex.annotation.sub_codex.DataProxy;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface PreDataProxy {

    Class<? extends DataProxy<?>> value();

}
