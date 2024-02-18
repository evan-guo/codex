package com.codex.core.scan;

import com.codex.annotation.config.Comment;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author evanguo
 */
@Import({CodexScannerConfigurerRegistrar.class})
@EnableAutoConfiguration
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Comment("Codex项目包扫描核心注解")
public @interface CodexScan {
    @Comment("需要被扫描的包名")
    String[] value() default {};
}