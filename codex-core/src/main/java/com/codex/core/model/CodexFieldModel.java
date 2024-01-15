package com.codex.core.model;

import com.codex.annotation.CodexField;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

/**
 * Codex注解字段存储
 *
 * @author evanguo
 */
@Getter
@Setter
public class CodexFieldModel {

    private transient Field field;

    private transient CodexField eruptField;

    private String fieldName;

    private transient String fieldReturnName;

    private Object value;

    public CodexFieldModel(Field field) {
        this.field = field;
        this.eruptField = field.getAnnotation(CodexField.class);
        this.fieldName = field.getName();
        this.fieldReturnName = field.getType().getSimpleName();
    }

}
