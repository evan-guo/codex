package com.codex.security.util;

import com.codex.security.exception.SecurityException;
import lombok.SneakyThrows;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 校验工具，被校验的Bean的字段属性需使用javax.validation系列注解
 *
 * @author evan guo
 * @since 2023-01-16
 */
public class ValidationUtil {

    private static final Validator VALIDATOR;

    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            VALIDATOR = factory.getValidator();
        }
    }

    /**
     * 校验Bean，返回错误Map
     *
     * @param bean   待校验对象
     * @param groups 分组
     */
    public static <T> Map<String, String> validate(T bean, Class<?>... groups) {
        Set<ConstraintViolation<T>> set = VALIDATOR.validate(bean, groups);
        if (set.isEmpty()) {
            return Collections.emptyMap();
        } else {
            return set.stream().collect(Collectors.toMap(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage));
        }
    }

    /**
     * 校验Bean，抛出BindException异常
     *
     * @param bean   待校验对象
     * @param groups 分组
     */
    @SneakyThrows
    public static <T> void validateThrow(T bean, Class<?>... groups) {
        Set<ConstraintViolation<T>> set = VALIDATOR.validate(bean, groups);
        if (!set.isEmpty()) {
            String errorMsg = set.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
            throw new SecurityException(errorMsg);
        }
    }

}
