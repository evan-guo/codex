package com.codex.core.model;

import com.codex.annotation.Codex;
import com.codex.annotation.CodexField;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Codex注解类存储
 *
 * @author evanguo
 */
@Getter
@Setter
public class CodexModel {
    /**
     * 类对象
     */
    private transient Class<?> clazz;
    /**
     * 注解对象
     */
    private transient Codex codex;
    /**
     * 类中使用了@CodexField注解的字段名与CodexField注解对象的映射
     */
    private transient Map<String, CodexFieldModel> codexFieldMap;
    /**
     * 类名
     */
    private String clazzName;
    /**
     * 类中使用了@CodexField注解的字段列表
     */
    private List<CodexFieldModel> codexFieldModels;

    public CodexModel(Class<?> codexClazz) {
        this.clazz = codexClazz;
        this.codex = codexClazz.getAnnotation(Codex.class);
        this.clazzName = codexClazz.getSimpleName();
    }

}
