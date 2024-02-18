package com.codex.core.scan;

import com.codex.annotation.CodexField;
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
     * 字段返回值
     */
    private transient String fieldReturnName;
    /**
     * 列名
     */
    private String columnName;

    public CodexFieldModel(Field field) {
        this.field = field;
        this.codexField = field.getAnnotation(CodexField.class);
        this.fieldName = field.getName();
        this.fieldReturnName = field.getType().getSimpleName();
    }

}
