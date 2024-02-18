package com.codex.core.scan;

import com.codex.annotation.Codex;
import com.mybatisflex.core.table.TableInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Codex注解类存储模型
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
     * 类名
     */
    private String clazzName;
    /**
     * 注解对象
     */
    private transient Codex codex;
    /**
     * 类中使用了@CodexField注解的字段列表
     */
    private List<CodexFieldModel> codexFieldModels;
    /**
     * 类中使用了@CodexField注解的字段名与CodexField注解对象的映射
     */
    private transient Map<String, CodexFieldModel> codexFieldMap;
    /**
     * 开启搜索的字段列表
     */
    private List<CodexFieldModel> searchFieldModels;
    /**
     * 表信息
     */
    private TableInfo tableInfo;

    public CodexModel(Class<?> codexClazz) {
        this.clazz = codexClazz;
        this.codex = codexClazz.getAnnotation(Codex.class);
        this.clazzName = codexClazz.getSimpleName();
        this.codexFieldModels = new ArrayList<>();
        this.searchFieldModels = new ArrayList<>();
        this.codexFieldMap = new LinkedCaseInsensitiveMap<>();
    }

}
