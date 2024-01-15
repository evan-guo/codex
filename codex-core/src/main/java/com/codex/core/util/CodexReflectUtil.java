package com.codex.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.map.WeakConcurrentMap;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author evan guo
 */
public class CodexReflectUtil {

    private static final WeakConcurrentMap<Class<?>, List<Field>> FIELDS_CACHE = new WeakConcurrentMap<>();

    public static List<Field> getFields(Class<?> beanClass, Predicate<Field> fieldFilter) throws SecurityException {
        return getFields(beanClass).stream().filter(fieldFilter).collect(Collectors.toList());
    }

    public static List<Field> getFields(Class<?> beanClass) throws SecurityException {
        Assert.notNull(beanClass);
        return FIELDS_CACHE.computeIfAbsent(beanClass, () -> getFieldsDirectly(beanClass, true));
    }

    public static List<Field> getFieldsDirectly(Class<?> beanClass, boolean withSuperClassFields) throws SecurityException {
        Assert.notNull(beanClass);
        List<Field> allFields = new ArrayList<>();
        for(Class<?> searchType = beanClass; searchType != null; searchType = withSuperClassFields ? searchType.getSuperclass() : null) {
            Field[] declaredFields = searchType.getDeclaredFields();
            allFields.addAll(0, List.of(declaredFields));
        }
        return allFields;
    }

}
