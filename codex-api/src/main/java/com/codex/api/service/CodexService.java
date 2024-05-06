package com.codex.api.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.codex.api.annotation.PowerObject;
import com.codex.api.annotation.Search;
import com.codex.api.annotation.SearchType;
import com.codex.api.model.vo.PageQueryVo;
import com.codex.api.proxy.DataProxyInvoke;
import com.codex.api.scan.CodexScanner;
import com.codex.api.scan.CodexFieldModel;
import com.codex.api.scan.CodexModel;
import com.codex.api.util.PowerUtil;
import com.codex.mapper.CodexMapper;
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
import java.util.function.Function;

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
    public <T> Page<T> page(Class<T> codexClass, PageQueryVo queryVo, Boolean withRelation) {
        CodexModel codexModel = getCodexModel(codexClass, PowerObject::isQuery);
        BaseMapper<T> baseMapper = getBaseMapper(codexClass);
        QueryWrapper queryWrapper = getQueryWrapper(codexModel, queryVo.getConditions());
        DataProxyInvoke.invoke(codexModel, dataProxy -> dataProxy.beforeFetch(queryVo.getConditions(), queryWrapper));
        Page<T> paginate;
        if (withRelation) {
            if (baseMapper != null) {
                paginate = baseMapper.paginateWithRelations(queryVo.getPageNumber(), queryVo.getPageSize(), queryWrapper);
            } else {
                paginate = codexMapper.paginateWithRelations(codexClass, queryVo.getPageNumber(), queryVo.getPageSize(), queryWrapper);
            }
        } else {
            if (baseMapper != null) {
                paginate = baseMapper.paginate(queryVo.getPageNumber(), queryVo.getPageSize(), queryWrapper);
            } else {
                paginate = codexMapper.paginate(codexClass, queryVo.getPageNumber(), queryVo.getPageSize(), queryWrapper);
            }
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
    public <T> List<T> list(Class<T> codexClass, JSONObject conditions, Boolean withRelation) {
        CodexModel codexModel = getCodexModel(codexClass, PowerObject::isQuery);
        BaseMapper<T> baseMapper = getBaseMapper(codexClass);
        QueryWrapper wrapper = getQueryWrapper(codexModel, conditions);
        DataProxyInvoke.invoke(codexModel, dataProxy -> dataProxy.beforeFetch(conditions, wrapper));
        List<T> list;
        if (withRelation) {
            if (baseMapper != null) {
                list = baseMapper.selectListWithRelationsByQuery(wrapper);
            } else {
                list = codexMapper.selectListWithRelationsByQuery(codexClass, wrapper);
            }
        } else {
            if (baseMapper != null) {
                list = baseMapper.selectListByQuery(wrapper);
            } else {
                list = codexMapper.selectListByQuery(codexClass, wrapper);
            }
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
    public <T> T getById(Class<T> codexClass, Serializable id, Boolean withRelation) {
        CodexModel codexModel = getCodexModel(codexClass, PowerObject::isQuery);
        BaseMapper<T> baseMapper = getBaseMapper(codexClass);
        if (withRelation) {
            if (baseMapper != null) {
                return baseMapper.selectOneWithRelationsById(id);
            } else {
                return codexMapper.selectOneWithRelationsById(codexClass, id);
            }
        } else {
            if (baseMapper != null) {
                return baseMapper.selectOneById(id);
            } else {
                return codexMapper.selectOneById(codexClass, id);
            }
        }
    }

    /**
     * 保存数据
     *
     * @param codexClass Codex类
     * @param data       数据
     */
    public <T> int save(Class<T> codexClass, Object data) {
        CodexModel codexModel = getCodexModel(codexClass, PowerObject::isAdd);
        T entity;
        if (data instanceof JSONObject) {
            entity = ((JSONObject) data).toJavaObject(codexClass);
        } else {
            entity = BeanUtil.toBean(data, codexClass);
        }
        DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.beforeAdd(data, entity)));
        BaseMapper<T> baseMapper = getBaseMapper(codexClass);
        int code;
        if (baseMapper != null) {
            code = baseMapper.insert(entity);
        } else {
            code = codexMapper.insert(codexClass, entity);
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
        CodexModel codexModel = getCodexModel(codexClass, PowerObject::isAdd);
        List<T> entities = BeanUtil.copyToList(dataList, codexClass);
        BaseMapper<T> baseMapper = getBaseMapper(codexClass);
        for (int i = 0; i < entities.size(); i++) {
            int finalI = i;
            DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.beforeAdd(dataList.get(finalI), entities.get(finalI))));
        }
        int result;
        if (baseMapper != null) {
            result = baseMapper.insertBatch(entities);
        } else {
            result = codexMapper.insertBatch(codexClass, entities);
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
        CodexModel codexModel = getCodexModel(codexClass, PowerObject::isEdit);
        T entity;
        if (data instanceof JSONObject) {
            entity = ((JSONObject) data).toJavaObject(codexClass);
        } else {
            entity = BeanUtil.toBean(data, codexClass);
        }
        DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.beforeEdit(data, entity)));
        BaseMapper<T> baseMapper = getBaseMapper(codexClass);
        int result;
        if (baseMapper != null) {
            result = baseMapper.update(entity, true);
        } else {
            result = codexMapper.update(codexClass, entity);
        }
        DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.afterEdit(entity)));
        return result;
    }

    /**
     * 批量修改数据
     *
     * @param codexClass Codex类
     * @param dataList   数据列表
     */
    public <T> int batchUpdate(Class<T> codexClass, List<Object> dataList) {
        CodexModel codexModel = getCodexModel(codexClass, PowerObject::isEdit);
        List<T> entities = BeanUtil.copyToList(dataList, codexClass);
        BaseMapper<T> baseMapper = getBaseMapper(codexClass);
        for (int i = 0; i < entities.size(); i++) {
            T entity = entities.get(i);
            int finalI = i;
            DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.beforeEdit(dataList.get(finalI), entity)));
            if (baseMapper != null) {
                baseMapper.update(entity, true);
            } else {
                codexMapper.update(codexClass, entity);
            }
            DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.afterEdit(entity)));
        }
        return 0;
    }

    /**
     * 删除数据
     *
     * @param codexClass Codex类
     * @param id         数据ID
     */
    public <T> int delete(Class<T> codexClass, Serializable id) {
        CodexModel codexModel = getCodexModel(codexClass, PowerObject::isDelete);
        BaseMapper<T> baseMapper = getBaseMapper(codexClass);
        T info = this.getById(codexClass, id, false);
        DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.beforeDelete(id)));
        int result;
        if (baseMapper != null) {
            result = baseMapper.deleteById(id);
        } else {
            result = codexMapper.deleteById(codexClass, id);
        }
        DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.afterDelete(info)));
        return result;
    }

    /**
     * 批量删除
     *
     * @param codexClass Codex类
     * @param ids        ID列表
     */
    public <T> int batchDelete(Class<T> codexClass, List<Serializable> ids) {
        CodexModel codexModel = getCodexModel(codexClass, PowerObject::isDelete);
        BaseMapper<T> baseMapper = getBaseMapper(codexClass);
        ids.forEach(id -> DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.beforeDelete(id))));
        int result;
        if (baseMapper != null) {
            result = baseMapper.deleteBatchByIds(ids);
        } else {
            result = codexMapper.deleteBatchByIds(codexClass, ids);
        }
        ids.forEach(id -> DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.afterDelete(id))));
        return result;
    }

    private CodexModel getCodexModel(Class<?> codexClass, Function<PowerObject, Boolean> powerFunc) {
        CodexModel codexModel = CodexScanner.getCodexModel(codexClass.getSimpleName());
        PowerUtil.powerLegal(codexModel, powerFunc);
        return codexModel;
    }

    private <T> BaseMapper<T> getBaseMapper(Class<T> clazz) {
        try {
            return Mappers.ofEntityClass(clazz);
        } catch (MybatisFlexException e) {
            log.debug("未找到{}的实现Mapper，使用CodexMapper", clazz.getSimpleName());
        }
        return null;
    }

    private QueryWrapper getQueryWrapper(CodexModel codexModel, JSONObject conditions) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.from(codexModel.getClazz());
        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                CodexFieldModel fieldModel = codexModel.getCodexFieldMap().get(entry.getKey());
                if (fieldModel != null && fieldModel.getCodexField().search().value() && StringUtils.hasText(fieldModel.getFieldName())) {
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
