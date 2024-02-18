package com.codex.core.scan;

import com.codex.core.CodexAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 *
 * 在SpringBoot启动时，从启动类上提取@CodexScan注解，通过CodexScan的值加载Codex类，如果未定义value，默认扫描启动类下的目录
 * 实现{@link ImportBeanDefinitionRegistrar}允许在导入配置类时动态注册额外的BeanDefinition
 *
 * @author evan guo
 * @since 1.0
 */
public class CodexScannerConfigurerRegistrar implements BeanFactoryAware, EnvironmentAware, ImportBeanDefinitionRegistrar {
    private final Logger logger = LoggerFactory.getLogger(CodexAutoConfiguration.class);
    private BeanFactory beanFactory;
    private Environment environment;
    public static List<String> static_packages;

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if (!AutoConfigurationPackages.has(this.beanFactory)) {
            logger.warn("Could not determine auto-configuration package, automatic scanning disabled.");
        } else {

            if (!isEnabled()) {
                logger.warn("The Codex is disabled, stop scanning Codex class. If you want Codex to start working, configure codex.enable = true.");
                return;
            }
            logger.debug("Searching for class annotated with @Codex");
            List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
            if (logger.isDebugEnabled()) {
                packages.forEach((pkg) -> logger.debug("Using auto-configuration base package '{}'", pkg));
            }
            CodexScannerConfigurerRegistrar.static_packages = packages;
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CodexScannerConfigurer.class);
            builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(packages));
            builder.setRole(2);
            registry.registerBeanDefinition(CodexScannerConfigurer.class.getName(), builder.getBeanDefinition());
        }
    }

    protected boolean isEnabled() {
        return this.getClass() == CodexScannerConfigurerRegistrar.class ? this.environment.getProperty("codex.enable", Boolean.class, true) : true;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
