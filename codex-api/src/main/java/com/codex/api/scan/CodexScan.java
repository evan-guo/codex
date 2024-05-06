package com.codex.api.scan;

import com.codex.api.annotation.Comment;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 通过该注解开启通用接口扫描
 *
 * @author evanguo
 */
@Import({CodexScannerConfigurerRegistrar.class})
@EnableAutoConfiguration
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface CodexScan {

    @Comment("需要被扫描的包名")
    String[] value() default {};

}