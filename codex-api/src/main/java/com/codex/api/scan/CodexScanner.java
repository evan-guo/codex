package com.codex.api.scan;

import cn.hutool.core.util.ModifierUtil;
import cn.hutool.core.util.ReflectUtil;
import com.codex.api.exception.CodexNotFoundException;
import lombok.Setter;
import lombok.SneakyThrows;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Codex类路径扫描器
 *
 * @author evan guo
 * @since 1.0.0
 */
public class CodexScanner extends ClassPathBeanDefinitionScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodexScanner.class);
    @Setter
    private Class<? extends Annotation> annotationClass;
    private static final Map<String, CodexModel> CODEX_MAP = new LinkedCaseInsensitiveMap<>();
    private static final Map<Class<?>, CodexModel> CODEX_CLASS_MAP = new HashMap<>();
    private static final List<CodexModel> CODEX_LIST = new ArrayList<>();

    public CodexScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public static List<CodexModel> getCodexModelList() {
        return CODEX_LIST;
    }

    public static CodexModel getCodexModel(String codexName) {
        CodexModel codexModel = CODEX_MAP.get(codexName);
        if (codexModel == null) {
            throw new CodexNotFoundException(codexName);
        }
        return codexModel;
    }

    public static CodexModel getCodexModel(Class<?> codexClass) {
        CodexModel codexModel = CODEX_CLASS_MAP.get(codexClass);
        if (codexModel == null) {
            throw new CodexNotFoundException(codexClass.getSimpleName());
        }
        return codexModel;
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            LOGGER.warn(() -> "No Codex class was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            this.processBeanDefinitions(beanDefinitions);
        }
        return beanDefinitions;
    }

    public void registerFilters() {
        if (this.annotationClass != null) {
            this.addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
        }
    }

    @SneakyThrows
    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitions) {
            BeanDefinition beanDefinition = beanDefinitionHolder.getBeanDefinition();
            Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
            CodexModel codexModel = initCodexModel(clazz);
            CODEX_LIST.add(codexModel);
            CODEX_MAP.put(clazz.getSimpleName(), codexModel);
            CODEX_CLASS_MAP.put(clazz, codexModel);
        }
    }

    /**
     * 构建Codex注解Model
     *
     * @param clazz 使用了Codex注解的类
     * @return Codex Model，里面包含了类信息、注解信息以及字段信息
     */
    private CodexModel initCodexModel(Class<?> clazz) {
        CodexModel codexModel = new CodexModel(clazz);
        // 获取字段信息
        Arrays.stream(ReflectUtil.getFields(clazz, field -> {
            boolean hasModifier = ModifierUtil.hasModifier(field, ModifierUtil.ModifierType.STATIC, ModifierUtil.ModifierType.ABSTRACT);
            return !hasModifier;
        })).forEach(field -> codexModel.addFieldModel(new CodexFieldModel(field)));
        return codexModel;
    }


}
