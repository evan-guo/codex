package com.codex.api;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author evan guo
 */
@ComponentScan
@EnableSpringUtil
//@EnableConfigurationProperties({CodexProperties.class})
@Configuration(proxyBeanMethods = false)
public class CodexApiAutoConfiguration {
}
