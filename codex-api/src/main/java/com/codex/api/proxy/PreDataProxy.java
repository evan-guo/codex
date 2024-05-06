package com.codex.api.proxy;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface PreDataProxy {

    Class<? extends DataProxy<?>> value();

}
