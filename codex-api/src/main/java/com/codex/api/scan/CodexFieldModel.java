package com.codex.api.scan;

import com.codex.api.annotation.CodexField;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

/**
 * Codex注解字段存储模型
 *
 * @author evanguo
 */
@Getter
@Setter
public class CodexFieldModel {
    /**
     * 字段信息
     */
    private transient Field field;
    /**
     * CodexField注解信息
     */
    private transient CodexField codexField;
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 字段类型
     */
    private transient String fieldTypeName;

    public CodexFieldModel(Field field) {
        this.field = field;
        this.codexField = field.getAnnotation(CodexField.class);
        this.fieldName = field.getName();
        this.fieldTypeName = field.getType().getSimpleName();
    }

}
