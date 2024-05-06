package com.codex.mapper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author evan guo
 */
@MapperScan("com.codex.mapper")
@ComponentScan
@Configuration(proxyBeanMethods = false)
public class MapperAutoConfiguration {
}
