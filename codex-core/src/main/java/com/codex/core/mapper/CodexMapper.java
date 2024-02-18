package com.codex.core.mapper;

import com.codex.core.config.CodexConstants;
import com.codex.core.mybatis.CodexEntitySqlProvider;
import com.mybatisflex.core.FlexConsts;
import com.mybatisflex.core.constant.FuncName;
import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.mybatis.MappedStatementTypes;
import com.mybatisflex.core.mybatis.Mappers;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.CPI;
import com.mybatisflex.core.query.FunctionQueryColumn;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Row;
import com.mybatisflex.core.table.TableInfo;
import com.mybatisflex.core.table.TableInfoFactory;
import com.mybatisflex.core.util.CollectionUtil;
import com.mybatisflex.core.util.ConvertUtil;
import com.mybatisflex.core.util.MapperUtil;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

import static com.mybatisflex.core.query.QueryMethods.count;

/**
 * Codex的通用Mapper
 *
 * @author evan guo
 */
@Mapper
public interface CodexMapper {

    /**
     * 默认批量处理切片数量。
     */
    int DEFAULT_BATCH_SIZE = 1000;

    /**
     * 插入实体类数据，不忽略 {@code null} 值。
     *
     * @param entity 实体类
     * @return 受影响的行数
     */
    default <T> int insert(Class<T> codexClass, T entity) {
        return insert(codexClass, entity, false);
    }

    /**
     * 插入实体类数据，但是忽略 {@code null} 的数据，只对有值的内容进行插入。
     * 这样的好处是数据库已经配置了一些默认值，这些默认值才会生效。
     *
     * @param entity 实体类
     * @return 受影响的行数
     */
    default <T> int insertSelective(Class<T> codexClass, T entity) {
        return insert(codexClass, entity, true);
    }

    /**
     * 插入实体类数据。
     *
     * @param entity      实体类
     * @param ignoreNulls 是否忽略 {@code null} 值
     * @return 受影响的行数
     * @see CodexEntitySqlProvider#insert(Map, ProviderContext)
     */
    @InsertProvider(type = CodexEntitySqlProvider.class, method = "insert")
    <T> int insert(@Param(CodexConstants.CLASS) Class<T> codexClass, @Param(FlexConsts.ENTITY) T entity, @Param(FlexConsts.IGNORE_NULLS) boolean ignoreNulls);

    /**
     * 插入带有主键的实体类，不忽略 {@code null} 值。
     *
     * @param entity 实体类
     * @return 受影响的行数
     */
    default <T> int insertWithPk(Class<T> codexClass, T entity) {
        return insertWithPk(codexClass, entity, false);
    }

    /**
     * 插入带有主键的实体类，忽略 {@code null} 值。
     *
     * @param entity 实体类
     * @return 受影响的行数
     */
    default <T> int insertSelectiveWithPk(Class<T> codexClass, T entity) {
        return insertWithPk(codexClass, entity, true);
    }

    /**
     * 带有主键的插入，此时实体类不会经过主键生成器生成主键。
     *
     * @param entity      带有主键的实体类
     * @param ignoreNulls 是否忽略 {@code null} 值
     * @return 受影响的行数
     * @see CodexEntitySqlProvider#insertWithPk(Map, ProviderContext)
     */
    @InsertProvider(type = CodexEntitySqlProvider.class, method = "insertWithPk")
    <T> int insertWithPk(@Param(CodexConstants.CLASS) Class<T> codexClass, @Param(FlexConsts.ENTITY) T entity, @Param(FlexConsts.IGNORE_NULLS) boolean ignoreNulls);

    /**
     * 批量插入实体类数据，只会根据第一条数据来构建插入的字段内容。
     *
     * @param entities 插入的数据列表
     * @return 受影响的行数
     * @see CodexEntitySqlProvider#insertBatch(Map, ProviderContext)
     * @see com.mybatisflex.core.FlexConsts#METHOD_INSERT_BATCH
     */
    @InsertProvider(type = CodexEntitySqlProvider.class, method = FlexConsts.METHOD_INSERT_BATCH)
    <T> int insertBatch(@Param(CodexConstants.CLASS) Class<T> codexClass, @Param(FlexConsts.ENTITIES) List<T> entities);

    /**
     * 批量插入实体类数据，按 size 切分。
     *
     * @param entities 插入的数据列表
     * @param size     切分大小
     * @return 受影响的行数
     */
    default <T> int insertBatch(Class<T> codexClass, List<T> entities, int size) {
        if (size <= 0) {
            size = DEFAULT_BATCH_SIZE;
        }
        int sum = 0;
        int entitiesSize = entities.size();
        int maxIndex = entitiesSize / size + (entitiesSize % size == 0 ? 0 : 1);
        for (int i = 0; i < maxIndex; i++) {
            List<T> list = entities.subList(i * size, Math.min(i * size + size, entitiesSize));
            sum += insertBatch(codexClass, list);
        }
        return sum;
    }

    /**
     * 插入或者更新，若主键有值，则更新，若没有主键值，则插入，插入或者更新都不会忽略 {@code null} 值。
     *
     * @param entity 实体类
     * @return 受影响的行数
     */
    default <T> int insertOrUpdate(Class<T> codexClass, T entity) {
        return insertOrUpdate(codexClass, entity, false);
    }

    /**
     * 插入或者更新，若主键有值，则更新，若没有主键值，则插入，插入或者更新都会忽略 {@code null} 值。
     *
     * @param entity 实体类
     * @return 受影响的行数
     */
    default <T> int insertOrUpdateSelective(Class<T> codexClass, T entity) {
        return insertOrUpdate(codexClass, entity, true);
    }

    /**
     * 插入或者更新，若主键有值，则更新，若没有主键值，则插入。
     *
     * @param entity      实体类
     * @param ignoreNulls 是否忽略 {@code null} 值
     * @return 受影响的行数
     */
    default <T> int insertOrUpdate(Class<T> codexClass, T entity, boolean ignoreNulls) {
        TableInfo tableInfo = TableInfoFactory.ofEntityClass(entity.getClass());
        Object[] pkArgs = tableInfo.buildPkSqlArgs(entity);
        if (pkArgs.length == 0 || pkArgs[0] == null) {
            return insert(codexClass, entity, ignoreNulls);
        } else {
            return update(codexClass, entity, ignoreNulls);
        }
    }

    /**
     * 根据主键删除数据。如果是多个主键的情况下，需要传入数组，例如：{@code new Integer[]{100,101}}。
     *
     * @param id 主键数据
     * @return 受影响的行数
     * @see CodexEntitySqlProvider#deleteById(Map, ProviderContext)
     */
    @DeleteProvider(type = CodexEntitySqlProvider.class, method = "deleteById")
    <T> int deleteById(@Param(CodexConstants.CLASS) Class<T> codexClass, @Param(FlexConsts.PRIMARY_VALUE) Serializable id);

    /**
     * 根据多个主键批量删除数据。
     *
     * @param ids 主键列表
     * @return 受影响的行数
     * @see CodexEntitySqlProvider#deleteBatchByIds(Map, ProviderContext)
     */
    @DeleteProvider(type = CodexEntitySqlProvider.class, method = "deleteBatchByIds")
    <T> int deleteBatchByIds(@Param(CodexConstants.CLASS) Class<T> codexClass, @Param(FlexConsts.PRIMARY_VALUE) Collection<? extends Serializable> ids);

    /**
     * 根据多个主键批量删除数据。
     *
     * @param ids  主键列表
     * @param size 切分大小
     * @return 受影响的行数
     * @see CodexEntitySqlProvider#deleteBatchByIds(Map, ProviderContext)
     */
    default <T> int deleteBatchByIds(Class<T> codexClass, List<? extends Serializable> ids, int size) {
        if (size <= 0) {
            size = DEFAULT_BATCH_SIZE;
        }
        int sum = 0;
        int entitiesSize = ids.size();
        int maxIndex = entitiesSize / size + (entitiesSize % size == 0 ? 0 : 1);
        for (int i = 0; i < maxIndex; i++) {
            List<? extends Serializable> list = ids.subList(i * size, Math.min(i * size + size, entitiesSize));
            sum += deleteBatchByIds(codexClass, list);
        }
        return sum;
    }

    /**
     * 根据查询条件来删除数据。
     *
     * @param queryWrapper 条件
     * @return 受影响的行数
     * @see CodexEntitySqlProvider#deleteByQuery(Map, ProviderContext)
     */
    @DeleteProvider(type = CodexEntitySqlProvider.class, method = "deleteByQuery")
    <T> int deleteByQuery(@Param(CodexConstants.CLASS) Class<T> codexClass, @Param(FlexConsts.QUERY) QueryWrapper queryWrapper);

    /**
     * 根据主键来更新数据，若实体类属性数据为 {@code null}，该属性不会更新到数据库。
     *
     * @param entity 数据内容，必须包含有主键
     * @return 受影响的行数
     */
    default <T> int update(Class<T> codexClass, T entity) {
        return update(codexClass, entity, true);
    }

    /**
     * 根据主键来更新数据到数据库。
     *
     * @param entity      数据内容，必须包含有主键
     * @param ignoreNulls 是否忽略空内容字段
     * @return 受影响的行数
     * @see CodexEntitySqlProvider#update(Map, ProviderContext)
     */
    @UpdateProvider(type = CodexEntitySqlProvider.class, method = "update")
    <T> int update(@Param(CodexConstants.CLASS) Class<T> codexClass, @Param(FlexConsts.ENTITY) T entity, @Param(FlexConsts.IGNORE_NULLS) boolean ignoreNulls);

    /**
     * 根据查询条件来更新数据。
     *
     * @param entity       实体类
     * @param queryWrapper 条件
     * @return 受影响的行数
     */
    default <T> int updateByQuery(Class<T> codexClass, T entity, QueryWrapper queryWrapper) {
        return updateByQuery(codexClass, entity, true, queryWrapper);
    }

    /**
     * 根据查询条件来更新数据。
     *
     * @param entity       实体类
     * @param ignoreNulls  是否忽略空值
     * @param queryWrapper 条件
     * @return 受影响的行数
     * @see CodexEntitySqlProvider#updateByQuery(Map, ProviderContext)
     */
    @UpdateProvider(type = CodexEntitySqlProvider.class, method = "updateByQuery")
    <T> int updateByQuery(@Param(CodexConstants.CLASS) Class<T> codexClass, @Param(FlexConsts.ENTITY) T entity, @Param(FlexConsts.IGNORE_NULLS) boolean ignoreNulls, @Param(FlexConsts.QUERY) QueryWrapper queryWrapper);

    /**
     * 根据主键查询数据。
     *
     * @param id 主键
     * @return 实体类数据
     * @see CodexEntitySqlProvider#selectOneById(Map, ProviderContext)
     */
    @SelectProvider(type = CodexEntitySqlProvider.class, method = "selectOneById")
    <T> T selectOneById(@Param(CodexConstants.CLASS) Class<T> codexClass, @Param(FlexConsts.PRIMARY_VALUE) Serializable id);

    /**
     * 根据查询条件来查询 1 条数据。
     *
     * @param queryWrapper 条件
     * @return 实体类数据
     */
    default <T> T selectOneByQuery(Class<T> codexClass, QueryWrapper queryWrapper) {
        queryWrapper.limit(1);
        return MapperUtil.getSelectOneResult(selectListByQuery(codexClass, queryWrapper));
    }

    /**
     * 根据查询条件来查询 1 条数据。
     *
     * @param queryWrapper 条件
     * @param asType       接收数据类型
     * @return 实体类数据
     */
    default <R, T> R selectOneByQueryAs(Class<T> codexClass, QueryWrapper queryWrapper, Class<R> asType) {
        return MapperUtil.getSelectOneResult(selectListByQueryAs(codexClass, queryWrapper, asType));
    }


    /**
     * 根据查询条件来查询 1 条数据。
     *
     * @param queryWrapper 条件
     * @return 实体类数据
     */
    default <T> T selectOneWithRelationsByQuery(Class<T> codexClass, QueryWrapper queryWrapper) {
        CodexRelationMapper relationMapper = Mappers.ofMapperClass(CodexRelationMapper.class);
        relationMapper.setCodexClass(codexClass);
        return MapperUtil.queryRelations(relationMapper, MapperUtil.getSelectOneResult(selectListByQuery(codexClass, queryWrapper)));
    }

    /**
     * 根据主表主键来查询 1 条数据。
     *
     * @param id 主表主键
     * @return 实体类数据
     */
    default <T> T selectOneWithRelationsById(Class<T> codexClass, Serializable id) {
        CodexRelationMapper relationMapper = Mappers.ofMapperClass(CodexRelationMapper.class);
        relationMapper.setCodexClass(codexClass);
        return MapperUtil.queryRelations(relationMapper, selectOneById(codexClass, id));
    }

    /**
     * 根据主表主键来查询 1 条数据。
     *
     * @param id     表主键
     * @param asType 接收数据类型
     * @return 实体类数据
     */
    default <R, T> R selectOneWithRelationsByIdAs(Class<T> codexClass, Serializable id, Class<R> asType) {
        try {
            MappedStatementTypes.setCurrentType(asType);
            return (R) selectOneWithRelationsById(codexClass, id);
        } finally {
            MappedStatementTypes.clear();
        }
    }

    /**
     * 根据查询条件来查询 1 条数据。
     *
     * @param queryWrapper 条件
     * @param asType       接收数据类型
     * @return 实体类数据
     */
    default <R, T> R selectOneWithRelationsByQueryAs(Class<T> codexClass, QueryWrapper queryWrapper, Class<R> asType) {
        CodexRelationMapper relationMapper = Mappers.ofMapperClass(CodexRelationMapper.class);
        relationMapper.setCodexClass(codexClass);
        return MapperUtil.queryRelations(relationMapper, MapperUtil.getSelectOneResult(selectListByQueryAs(codexClass, queryWrapper, asType)));
    }

    /**
     * 根据多个主键来查询多条数据。
     *
     * @param ids 主键列表
     * @return 数据列表
     * @see CodexEntitySqlProvider#selectListByIds(Map, ProviderContext)
     */
    @SelectProvider(type = CodexEntitySqlProvider.class, method = "selectListByIds")
    <T> List<T> selectListByIds(@Param(CodexConstants.CLASS) Class<T> codexClass, @Param(FlexConsts.PRIMARY_VALUE) Collection<? extends Serializable> ids);

    /**
     * 根据查询条件查询数据列表。
     *
     * @param queryWrapper 条件
     * @return 数据列表
     * @see CodexEntitySqlProvider#selectListByQuery(Map, ProviderContext)
     */
    @SelectProvider(type = CodexEntitySqlProvider.class, method = "selectListByQuery")
    <T> List<T> selectListByQuery(@Param(CodexConstants.CLASS) Class<T> codexClass, @Param(FlexConsts.QUERY) QueryWrapper queryWrapper);

    /**
     * 根据查询条件查询 Row 数据。
     *
     * @param queryWrapper 条件
     * @return 行数据
     */
    @SelectProvider(type = CodexEntitySqlProvider.class, method = "selectListByQuery")
    <T> List<Row> selectRowsByQuery(@Param(CodexConstants.CLASS) Class<T> codexClass, @Param(FlexConsts.QUERY) QueryWrapper queryWrapper);

    /**
     * 根据查询条件查询数据列表，要求返回的数据为 asType。这种场景一般用在 left join 时，
     * 有多出了实体类本身的字段内容，可以转换为 dto、vo 等场景。
     *
     * @param queryWrapper 条件
     * @param asType       接收数据类型
     * @return 数据列表
     */
    default <R, T> List<R> selectListByQueryAs(Class<T> codexClass, QueryWrapper queryWrapper, Class<R> asType) {
        if (Number.class.isAssignableFrom(asType)
                || String.class == asType) {
            return selectObjectListByQueryAs(codexClass, queryWrapper, asType);
        }

        if (Map.class.isAssignableFrom(asType)) {
            return (List<R>) selectRowsByQuery(codexClass, queryWrapper);
        }

        try {
            MappedStatementTypes.setCurrentType(asType);
            return (List<R>) selectListByQuery(codexClass, queryWrapper);
        } finally {
            MappedStatementTypes.clear();
        }
    }

    /**
     * 查询实体类及其 Relation 注解字段。
     *
     * @param queryWrapper 条件
     */
    default <T> List<T> selectListWithRelationsByQuery(Class<T> codexClass, QueryWrapper queryWrapper) {
        CodexRelationMapper relationMapper = Mappers.ofMapperClass(CodexRelationMapper.class);
        relationMapper.setCodexClass(codexClass);
        return MapperUtil.queryRelations(relationMapper, selectListByQuery(codexClass, queryWrapper));
    }

    /**
     * 查询实体类及其 Relation 注解字段。
     *
     * @param queryWrapper 条件
     * @param asType       要求返回的数据类型
     * @return 数据列表
     */
    default <R, T> List<R> selectListWithRelationsByQueryAs(Class<T> codexClass, QueryWrapper queryWrapper, Class<R> asType) {
        if (Number.class.isAssignableFrom(asType)
                || String.class == asType) {
            return selectObjectListByQueryAs(codexClass, queryWrapper, asType);
        }

        if (Map.class.isAssignableFrom(asType)) {
            return (List<R>) selectRowsByQuery(codexClass, queryWrapper);
        }
        CodexRelationMapper relationMapper = Mappers.ofMapperClass(CodexRelationMapper.class);
        relationMapper.setCodexClass(codexClass);
        List<T> result;
        try {
            MappedStatementTypes.setCurrentType(asType);
            result = selectListByQuery(codexClass, queryWrapper);
        } finally {
            MappedStatementTypes.clear();
        }
        return MapperUtil.queryRelations(relationMapper, (List<R>) result);
    }

    /**
     * 查询实体类及其 Relation 注解字段。
     *
     * @param queryWrapper 条件
     * @param asType       返回的类型
     * @param consumers    字段查询
     * @return 数据列表
     */
    default <R, T> List<R> selectListWithRelationsByQueryAs(Class<T> codexClass, QueryWrapper queryWrapper, Class<R> asType, Consumer<FieldQueryBuilder<R>>... consumers) {
        CodexRelationMapper relationMapper = Mappers.ofMapperClass(CodexRelationMapper.class);
        relationMapper.setCodexClass(codexClass);
        List<R> list = selectListByQueryAs(codexClass, queryWrapper, asType);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        } else {
            MapperUtil.queryRelations(relationMapper, list);
            MapperUtil.queryFields(relationMapper, list, consumers);
            return list;
        }
    }

    /**
     * 查询全部数据。
     *
     * @return 数据列表
     */
    default <T> List<T> selectAll(Class<T> codexClass) {
        return selectListByQuery(codexClass, new QueryWrapper());
    }

    /**
     * 查询全部数据，及其 Relation 字段内容。
     *
     * @return 数据列表
     */
    default <T> List<T> selectAllWithRelations(Class<T> codexClass) {
        CodexRelationMapper relationMapper = Mappers.ofMapperClass(CodexRelationMapper.class);
        relationMapper.setCodexClass(codexClass);
        return MapperUtil.queryRelations(relationMapper, selectListByQuery(codexClass, new QueryWrapper()));
    }

    /**
     * 查询第一列返回的数据集合，QueryWrapper 执行的结果应该只有 1 列，例如：<br>
     * {@code QueryWrapper.create().select(ACCOUNT.id).where(...);}
     *
     * @param queryWrapper 查询包装器
     * @return 数据列表
     * @see CodexEntitySqlProvider#selectObjectByQuery(Map, ProviderContext)
     */
    @SelectProvider(type = CodexEntitySqlProvider.class, method = "selectObjectByQuery")
    <T> List<Object> selectObjectListByQuery(@Param(CodexConstants.CLASS) Class<T> codexClass, @Param(FlexConsts.QUERY) QueryWrapper queryWrapper);

    /**
     * 查询第一列返回的数据集合，QueryWrapper 执行的结果应该只有 1 列，例如：<br>
     * {@code QueryWrapper.create().select(ACCOUNT.id).where(...);}
     *
     * @param queryWrapper 查询包装器
     * @param asType       转换成的数据类型
     * @return 数据列表
     */
    default <R, T> List<R> selectObjectListByQueryAs(Class<T> codexClass, QueryWrapper queryWrapper, Class<R> asType) {
        List<Object> queryResults = selectObjectListByQuery(codexClass, queryWrapper);
        if (queryResults == null || queryResults.isEmpty()) {
            return Collections.emptyList();
        }
        List<R> results = new ArrayList<>(queryResults.size());
        for (Object queryResult : queryResults) {
            results.add((R) ConvertUtil.convert(queryResult, asType));
        }
        return results;
    }

    /**
     * 查询数据量。
     *
     * @param queryWrapper 条件
     * @return 数据量
     */
    default <T> long selectCountByQuery(Class<T> codexClass, QueryWrapper queryWrapper) {
        List<QueryColumn> selectColumns = CPI.getSelectColumns(queryWrapper);
        try {
            List<Object> objects;
            if (CollectionUtil.isEmpty(selectColumns)) {
                // 未设置 COUNT(...) 列，默认使用 COUNT(*) 查询
                queryWrapper.select(count());
                objects = selectObjectListByQuery(codexClass, queryWrapper);
            } else if (selectColumns.get(0) instanceof FunctionQueryColumn) {
                // COUNT 函数必须在第一列
                if (!FuncName.COUNT.equalsIgnoreCase(
                        ((FunctionQueryColumn) selectColumns.get(0)).getFnName()
                )) {
                    // 第一个查询列不是 COUNT 函数，使用 COUNT(*) 替换所有的查询列
                    CPI.setSelectColumns(queryWrapper, Collections.singletonList(count()));
                }
                // 第一个查询列是 COUNT 函数，可以使用 COUNT(1)、COUNT(列名) 代替默认的 COUNT(*)
                objects = selectObjectListByQuery(codexClass, queryWrapper);
            } else {
                // 查询列中的第一列不是 COUNT 函数
                if (MapperUtil.hasDistinct(selectColumns)) {
                    // 查询列中包含 DISTINCT 去重
                    // 使用子查询 SELECT COUNT(*) FROM (SELECT DISTINCT ...) AS `t`
                    objects = selectObjectListByQuery(codexClass, MapperUtil.rawCountQueryWrapper(queryWrapper));
                } else {
                    // 使用 COUNT(*) 替换所有的查询列
                    CPI.setSelectColumns(queryWrapper, Collections.singletonList(count()));
                    objects = selectObjectListByQuery(codexClass, queryWrapper);
                }
            }
            return MapperUtil.getLongNumber(objects);
        } finally {
            //fixed https://github.com/mybatis-flex/mybatis-flex/issues/49
            CPI.setSelectColumns(queryWrapper, selectColumns);
        }
    }

    /**
     * 分页查询。
     *
     * @param pageNumber   当前页码
     * @param pageSize     每页的数据量
     * @param queryWrapper 条件
     * @return 分页数据
     */
    default <T> Page<T> paginate(Class<T> codexClass, Number pageNumber, Number pageSize, QueryWrapper queryWrapper) {
        Page<T> page = new Page<>(pageNumber, pageSize);
        return paginate(codexClass, page, queryWrapper);
    }

    /**
     * 分页查询，及其 Relation 字段内容。
     *
     * @param pageNumber   当前页码
     * @param pageSize     每页的数据量
     * @param queryWrapper 条件
     * @return 分页数据
     */
    default <T> Page<T> paginateWithRelations(Class<T> codexClass, Number pageNumber, Number pageSize, QueryWrapper queryWrapper) {
        Page<T> page = new Page<>(pageNumber, pageSize);
        return paginateWithRelations(codexClass, page, queryWrapper);
    }

    /**
     * 分页查询。
     *
     * @param page         包含了页码、每页的数据量，可能包含数据总量
     * @param queryWrapper 条件
     * @return page 数据
     */
    default <T> Page<T> paginate(Class<T> codexClass, Page<T> page, QueryWrapper queryWrapper) {
        return paginateAs(codexClass, page, queryWrapper, codexClass);
    }

    /**
     * 分页查询，及其 Relation 字段内容。
     *
     * @param page         包含了页码、每页的数据量，可能包含数据总量
     * @param queryWrapper 条件
     * @return 分页数据
     */
    default <T> Page<T> paginateWithRelations(Class<T> codexClass, Page<T> page, QueryWrapper queryWrapper) {
        return paginateWithRelationsAs(codexClass, page, queryWrapper, codexClass);
    }

    /**
     * 分页查询。
     *
     * @param pageNumber   当前页码
     * @param pageSize     每页的数据量
     * @param queryWrapper 条件
     * @param asType       接收数据类型
     * @return 分页数据
     */
    default <R, T> Page<R> paginateAs(Class<T> codexClass, Number pageNumber, Number pageSize, QueryWrapper queryWrapper, Class<R> asType) {
        CodexRelationMapper relationMapper = Mappers.ofMapperClass(CodexRelationMapper.class);
        relationMapper.setCodexClass(codexClass);
        Page<R> page = new Page<>(pageNumber, pageSize);
        return MapperUtil.doPaginate(relationMapper, page, queryWrapper, asType, false);
    }

    /**
     * 分页查询。
     *
     * @param page         包含了页码、每页的数据量，可能包含数据总量
     * @param queryWrapper 条件
     * @param asType       接收数据类型
     * @return 分页数据
     */
    default <R, T> Page<R> paginateAs(Class<T> codexClass, Page<R> page, QueryWrapper queryWrapper, Class<R> asType) {
        CodexRelationMapper relationMapper = Mappers.ofMapperClass(CodexRelationMapper.class);
        relationMapper.setCodexClass(codexClass);
        return MapperUtil.doPaginate(relationMapper, page, queryWrapper, asType, false);
    }

    /**
     * 分页查询，及其 Relation 字段内容。
     *
     * @param pageNumber   当前页码
     * @param pageSize     每页的数据量
     * @param queryWrapper 条件
     * @param asType       接收数据类型
     * @return 分页数据
     */
    default <R, T> Page<R> paginateWithRelationsAs(Class<T> codexClass, Number pageNumber, Number pageSize, QueryWrapper queryWrapper, Class<R> asType) {
        CodexRelationMapper relationMapper = Mappers.ofMapperClass(CodexRelationMapper.class);
        relationMapper.setCodexClass(codexClass);
        Page<R> page = new Page<>(pageNumber, pageSize);
        return MapperUtil.doPaginate(relationMapper, page, queryWrapper, asType, true);
    }

    /**
     * 分页查询，及其 Relation 字段内容。
     *
     * @param page         包含了页码、每页的数据量，可能包含数据总量
     * @param queryWrapper 条件
     * @param asType       接收数据类型
     * @return 分页数据
     */
    default <R, T> Page<R> paginateWithRelationsAs(Class<T> codexClass, Page<R> page, QueryWrapper queryWrapper, Class<R> asType) {
        CodexRelationMapper relationMapper = Mappers.ofMapperClass(CodexRelationMapper.class);
        relationMapper.setCodexClass(codexClass);
        return MapperUtil.doPaginate(relationMapper, page, queryWrapper, asType, true);
    }


}
