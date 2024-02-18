package com.codex.core.mapper;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.mybatis.Mappers;
import com.mybatisflex.core.query.QueryWrapper;

import java.util.List;

/**
 * 针对于Relation关联查询的Mapper
 *
 * @author evan guo
 * @since 1.0.0
 */
public interface CodexRelationMapper extends BaseMapper<Object> {

    ThreadLocal<Class<?>> CODEX_CLASS_THREAD_LOCAL = new ThreadLocal<>();

    default void setCodexClass(Class<?> codexClass) {
        CODEX_CLASS_THREAD_LOCAL.set(codexClass);
    }

    default void getCodexClass(Class<?> codexClass) {
        CODEX_CLASS_THREAD_LOCAL.get();
    }

    @Override
    default <R> List<R> selectListByQueryAs(QueryWrapper queryWrapper, Class<R> asType) {
        Class<?> codexClass = CODEX_CLASS_THREAD_LOCAL.get();
        CodexMapper codexMapper = Mappers.ofMapperClass(CodexMapper.class);
        CODEX_CLASS_THREAD_LOCAL.remove();
        return codexMapper.selectListByQueryAs(codexClass, queryWrapper, asType);
    }


}
