package com.codex.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.codex.annotation.sub_codex.PowerObject;
import com.codex.annotation.sub_field.Search;
import com.codex.annotation.sub_field.SearchType;
import com.codex.core.api.query.PageQueryVo;
import com.codex.core.scan.CodexFieldModel;
import com.codex.core.scan.CodexModel;
import com.codex.core.mapper.CodexMapper;
import com.codex.core.proxy.DataProxyInvoke;
import com.codex.core.scan.CodexScanner;
import com.codex.core.util.PowerUtil;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.exception.MybatisFlexException;
import com.mybatisflex.core.mybatis.Mappers;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * Codex的通用数据操作类
 *
 * @author evan guo
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class CodexService {

    private final CodexMapper codexMapper;

    /**
     * 分页查询
     *
     * @param codexClass Codex类
     * @param queryVo    分页查询条件
     * @return 分页查询结果
     */
    public <T> Page<T> page(Class<T> codexClass, PageQueryVo queryVo) {
        CodexModel codexModel = CodexScanner.getCodexModel(codexClass.getSimpleName());
        PowerUtil.powerLegal(codexModel, PowerObject::isQuery);
        BaseMapper<T> baseMapper = getBaseMapper(codexModel.getClazz());
        QueryWrapper wrapper = getWrapper(codexModel, queryVo.getConditions());
        DataProxyInvoke.invoke(codexModel, dataProxy -> dataProxy.beforeFetch(queryVo.getConditions(), wrapper));
        Page<T> paginate;
        if (baseMapper != null) {
            paginate = baseMapper.paginate(queryVo.getPageNumber(), queryVo.getPageSize(), wrapper);
        } else {
            paginate = codexMapper.paginate((Class<T>) codexModel.getClazz(), queryVo.getPageNumber(), queryVo.getPageSize(), wrapper);
        }
        DataProxyInvoke.invoke(codexModel, dataProxy -> dataProxy.afterFetch((List<Object>) paginate.getRecords()));
        return paginate;
    }


    public <T> Page<T> pageWithRelation(Class<T> codexClass, PageQueryVo queryVo) {
        CodexModel codexModel = CodexScanner.getCodexModel(codexClass);
        PowerUtil.powerLegal(codexModel, PowerObject::isQuery);
        BaseMapper<T> baseMapper = getBaseMapper(codexModel.getClazz());
        QueryWrapper wrapper = getWrapper(codexModel, queryVo.getConditions());
        DataProxyInvoke.invoke(codexModel, dataProxy -> dataProxy.beforeFetch(queryVo.getConditions(), wrapper));
        Page<T> paginate;
        if (baseMapper != null) {
            paginate = baseMapper.paginateWithRelations(queryVo.getPageNumber(), queryVo.getPageSize(), wrapper);
        } else {
            paginate = codexMapper.paginateWithRelations((Class<T>) codexModel.getClazz(), queryVo.getPageNumber(), queryVo.getPageSize(), wrapper);
        }
        DataProxyInvoke.invoke(codexModel, dataProxy -> dataProxy.afterFetch((List<Object>) paginate.getRecords()));
        return paginate;
    }

    /**
     * 列表查询
     *
     * @param codexClass Codex类
     * @param conditions 查询条件
     * @return 列表查询结果
     */
    public <T> List<T> list(Class<T> codexClass, JSONObject conditions) {
        CodexModel codexModel = CodexScanner.getCodexModel(codexClass);
        PowerUtil.powerLegal(codexModel, PowerObject::isQuery);
        BaseMapper<T> baseMapper = getBaseMapper(codexModel.getClazz());
        QueryWrapper wrapper = getWrapper(codexModel, conditions);
        DataProxyInvoke.invoke(codexModel, dataProxy -> dataProxy.beforeFetch(conditions, wrapper));
        List<T> list;
        if (baseMapper != null) {
            list = baseMapper.selectListByQuery(wrapper);
        } else {
            list = codexMapper.selectListByQuery((Class<T>) codexModel.getClazz(), wrapper);
        }
        DataProxyInvoke.invoke(codexModel, dataProxy -> dataProxy.afterFetch((List<Object>) list));
        return list;
    }


    public <T> List<T> listWithRelation(Class<T> codexClass, JSONObject conditions) {
        CodexModel codexModel = CodexScanner.getCodexModel(codexClass);
        PowerUtil.powerLegal(codexModel, PowerObject::isQuery);
        BaseMapper<T> baseMapper = getBaseMapper(codexModel.getClazz());
        QueryWrapper wrapper = getWrapper(codexModel, conditions);
        DataProxyInvoke.invoke(codexModel, dataProxy -> dataProxy.beforeFetch(conditions, wrapper));
        List<T> list;
        if (baseMapper != null) {
            list = baseMapper.selectListWithRelationsByQuery(wrapper);
        } else {
            list = codexMapper.selectListWithRelationsByQuery((Class<T>) codexModel.getClazz(), wrapper);
        }
        DataProxyInvoke.invoke(codexModel, dataProxy -> dataProxy.afterFetch((List<Object>) list));
        return list;
    }

    /**
     * 查询详情
     *
     * @param codexClass Codex类
     * @param id         数据ID
     * @return 查询结果
     */
    public <T> T getById(Class<T> codexClass, Serializable id) {
        CodexModel codexModel = CodexScanner.getCodexModel(codexClass);
        PowerUtil.powerLegal(codexModel, PowerObject::isQuery);
        BaseMapper<T> baseMapper = getBaseMapper(codexModel.getClazz());
        if (baseMapper != null) {
            return baseMapper.selectOneById(id);
        } else {
            return codexMapper.selectOneById((Class<T>) codexModel.getClazz(), id);
        }
    }

    public <T> T getByIdWithRelation(Class<T> codexClass, Serializable id) {
        CodexModel codexModel = CodexScanner.getCodexModel(codexClass);
        PowerUtil.powerLegal(codexModel, PowerObject::isQuery);
        BaseMapper<T> baseMapper = getBaseMapper(codexModel.getClazz());
        if (baseMapper != null) {
            return baseMapper.selectOneWithRelationsById(id);
        } else {
            return codexMapper.selectOneWithRelationsById((Class<T>) codexModel.getClazz(), id);
        }
    }

    /**
     * 保存数据
     *
     * @param codexClass Codex类
     * @param data       数据
     */
    public <T> int save(Class<T> codexClass, Object data) {
        CodexModel codexModel = CodexScanner.getCodexModel(codexClass);
        PowerUtil.powerLegal(codexModel, PowerObject::isAdd);
        Object entity;
        if (data instanceof JSONObject) {
            entity = ((JSONObject) data).toJavaObject(codexModel.getClazz());
        } else {
            entity = data;
        }
        DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.beforeAdd(data, entity)));
        BaseMapper<Object> baseMapper = getBaseMapper(codexModel.getClazz());
        int code;
        if (baseMapper != null) {
            code = baseMapper.insert(entity);
        } else {
            code = codexMapper.insert((Class<Object>) codexModel.getClazz(), entity);
        }
        DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.afterAdd(entity)));
        return code;
    }

    /**
     * 批量保存
     *
     * @param codexClass Codex类
     * @param dataList   数据列表
     */
    public <T> int batchSave(Class<T> codexClass, List<T> dataList) {
        CodexModel codexModel = CodexScanner.getCodexModel(codexClass);
        PowerUtil.powerLegal(codexModel, PowerObject::isAdd);
        List<T> entities = BeanUtil.copyToList(dataList, (Class<T>) codexModel.getClazz());
        BaseMapper<T> baseMapper = getBaseMapper(codexModel.getClazz());
        for (int i = 0; i < entities.size(); i++) {
            int finalI = i;
            DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.beforeAdd(dataList.get(finalI), entities.get(finalI))));
        }
        int result;
        if (baseMapper != null) {
            result = baseMapper.insertBatch(entities);
        } else {
            result = codexMapper.insertBatch((Class<T>) codexModel.getClazz(), entities);
        }
        entities.forEach(entity -> DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.afterAdd(entity))));
        return result;
    }

    /**
     * 修改数据
     *
     * @param codexClass Codex类
     * @param data       数据
     */
    public <T> int update(Class<T> codexClass, Object data) {
        CodexModel codexModel = CodexScanner.getCodexModel(codexClass);
        PowerUtil.powerLegal(codexModel, PowerObject::isEdit);
        T entity;
        if (data instanceof JSONObject) {
            entity = ((JSONObject) data).toJavaObject(codexClass);
        } else {
            entity = JSONObject.parseObject(JSONObject.toJSONString(data), codexClass);
        }
        DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.beforeEdit(data, entity)));
        BaseMapper<T> baseMapper = getBaseMapper(codexModel.getClazz());
        int result;
        if (baseMapper != null) {
            result = baseMapper.update(entity, true);
        } else {
            result = codexMapper.update(codexClass, entity);
        }
        DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.afterEdit(entity)));
        return result;
    }

    public <T> int batchUpdate(Class<T> codexClass, List<Object> dataList) {
        CodexModel codexModel = CodexScanner.getCodexModel(codexClass);
        PowerUtil.powerLegal(codexModel, PowerObject::isEdit);
        List<T> entities = BeanUtil.copyToList(dataList, (Class<T>) codexModel.getClazz());
        BaseMapper<T> baseMapper = getBaseMapper(codexModel.getClazz());
        for (int i = 0; i < entities.size(); i++) {
            T entity = entities.get(i);
            int finalI = i;
            DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.beforeEdit(dataList.get(finalI), entity)));
            if (baseMapper != null) {
                baseMapper.update(entity, true);
            } else {
                codexMapper.update((Class<T>) codexModel.getClazz(), entity);
            }
            DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.afterEdit(entity)));
        }
        return 0;
    }

    public <T> int delete(Class<T> codexClass, Serializable id) {
        CodexModel codexModel = CodexScanner.getCodexModel(codexClass);
        PowerUtil.powerLegal(codexModel, PowerObject::isDelete);
        BaseMapper<T> baseMapper = getBaseMapper(codexModel.getClazz());
        T info = this.getById(codexClass, id);
        DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.beforeDelete(id)));
        int result;
        if (baseMapper != null) {
            result = baseMapper.deleteById(id);
        } else {
            result = codexMapper.deleteById((Class<T>) codexModel.getClazz(), id);
        }
        DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.afterDelete(info)));
        return result;
    }

    public <T> int batchDelete(Class<T> codexClass, List<Serializable> ids) {
        CodexModel codexModel = CodexScanner.getCodexModel(codexClass);
        PowerUtil.powerLegal(codexModel, PowerObject::isDelete);
        BaseMapper<T> baseMapper = getBaseMapper(codexModel.getClazz());
        ids.forEach(id -> DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.beforeDelete(id))));
        int result;
        if (baseMapper != null) {
            result = baseMapper.deleteBatchByIds(ids);
        } else {
            result = codexMapper.deleteBatchByIds((Class<T>) codexModel.getClazz(), ids);
        }
        ids.forEach(id -> DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.afterDelete(id))));
        return result;
    }


    private <T> BaseMapper<T> getBaseMapper(Class<?> clazz) {
        try {
            return Mappers.ofEntityClass((Class<T>) clazz);
        } catch (MybatisFlexException e) {
            log.debug("未找到{}的实现Mapper，使用CodexMapper", clazz.getSimpleName());
        }
        return null;
    }

    private QueryWrapper getWrapper(CodexModel codexModel, JSONObject conditions) {
//        TableInfoFactory.ofEntityClass();
//        Object entity = conditions.toJavaObject(codexModel.getClazz());
//        QueryWrapper wrapper = QueryWrapper.create(entity);
//        return wrapper;
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.from(codexModel.getClazz());
        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                CodexFieldModel fieldModel = codexModel.getCodexFieldMap().get(entry.getKey());
                if (fieldModel != null && fieldModel.getCodexField().search().value() && StringUtils.hasText(fieldModel.getColumnName())) {
                    Search search = fieldModel.getCodexField().search();
                    matchSearchType(queryWrapper, search.type(), entry);
                }
            }
        }
        return queryWrapper;
    }


    private void matchSearchType(QueryWrapper queryWrapper, SearchType type, Map.Entry<String, Object> entry) {
        if (SearchType.LIKE.equals(type)) {
            queryWrapper.like(entry.getKey(), entry.getValue());
        } else if (SearchType.EQ.equals(type)) {
            queryWrapper.eq(entry.getKey(), entry.getValue());
        } else if (SearchType.GT.equals(type)) {
            queryWrapper.gt(entry.getKey(), entry.getValue());
        } else if (SearchType.GTE.equals(type)) {
            queryWrapper.ge(entry.getKey(), entry.getValue());
        } else if (SearchType.LT.equals(type)) {
            queryWrapper.lt(entry.getKey(), entry.getValue());
        } else if (SearchType.LTE.equals(type)) {
            queryWrapper.le(entry.getKey(), entry.getValue());
        } else if (SearchType.NE.equals(type)) {
            queryWrapper.ne(entry.getKey(), entry.getValue());
        } else if (SearchType.IN.equals(type)) {
            queryWrapper.in(entry.getKey(), entry.getValue());
        } else if (SearchType.NOT_IN.equals(type)) {
            queryWrapper.notIn(entry.getKey(), entry.getValue());
        } else if (SearchType.BETWEEN.equals(type)) {
            if (Collections.class.isAssignableFrom(entry.getValue().getClass()) || entry.getValue().getClass().isArray()) {
                JSONArray array = JSONArray.parse(JSONObject.toJSONString(entry.getValue()));
                queryWrapper.between(entry.getKey(), array.get(0), array.get(1));
            }
        } else if (SearchType.NOT_BETWEEN.equals(type)) {
            if (Collections.class.isAssignableFrom(entry.getValue().getClass()) || entry.getValue().getClass().isArray()) {
                JSONArray array = JSONArray.parse(JSONObject.toJSONString(entry.getValue()));
                queryWrapper.notBetween(entry.getKey(), array.get(0), array.get(1));
            }
        } else if (SearchType.IS_NULL.equals(type)) {
            queryWrapper.isNull(entry.getKey());
        } else if (SearchType.IS_NOT_NULL.equals(type)) {
            queryWrapper.isNotNull(entry.getKey());
        } else if (SearchType.NOT_LIKE.equals(type)) {
            queryWrapper.notLike(entry.getKey(), entry.getValue());
        }
    }

}
