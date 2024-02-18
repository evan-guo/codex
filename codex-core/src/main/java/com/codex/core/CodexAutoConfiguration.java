package com.codex.core;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.codex.core.properties.CodexProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * 用于指定spring自动扫描包并加载组件
 * 使用@ComponentScan指定spring扫描该目录及子目录下的组件
 * 通过在resources/META-INF/spring.factories文件内启用自动配置功能，让springboot查找到该类
 *
 * @author evan guo
 * @since 1.0
 */
@MapperScan("com.codex.core.mybatis")
@ComponentScan
@EnableSpringUtil
@EnableConfigurationProperties({CodexProperties.class})
@Configuration(proxyBeanMethods = false)
public class CodexAutoConfiguration {

}
