package com.codex.core.scan;

import com.codex.annotation.Codex;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author evan guo
 */
public class CodexScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean, BeanNameAware {
    private String beanName;
    @Setter
    @Getter
    private String basePackage;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.basePackage, "Property 'basePackage' is required");
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        BeanDefinition codexScannerBean = beanDefinitionRegistry.getBeanDefinition(beanName);
        PropertyValues values = codexScannerBean.getPropertyValues();
        this.basePackage = this.getPropertyValue("basePackage", values);;
        CodexScanner scanner = new CodexScanner(beanDefinitionRegistry);
        scanner.setAnnotationClass(Codex.class);
        scanner.registerFilters();
        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ",; \t\n"));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    private String getPropertyValue(String propertyName, PropertyValues values) {
        PropertyValue property = values.getPropertyValue(propertyName);
        if (property == null) {
            return null;
        } else {
            Object value = property.getValue();
            if (value == null) {
                return null;
            } else if (value instanceof String) {
                return value.toString();
            } else {
                return value instanceof TypedStringValue ? ((TypedStringValue)value).getValue() : null;
            }
        }
    }

}
