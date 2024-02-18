package com.codex.core.mybatis;

import com.mybatisflex.core.FlexConsts;
import com.mybatisflex.core.exception.FlexAssert;
import com.mybatisflex.core.exception.FlexExceptions;
import com.mybatisflex.core.exception.locale.LocalizedFormats;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Row;
import com.mybatisflex.core.table.TableInfo;
import com.mybatisflex.core.table.TableInfoFactory;
import com.mybatisflex.core.util.StringUtil;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.session.Configuration;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 拷贝MyBatis-Flex的ProviderUtil，因为其是受保护的，不对外开放
 *
 * @author evan guo
 * @since 1.0.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class PublicProviderUtil {

    public PublicProviderUtil() {
    }

    public static String getSqlString(Map params) {
        return (String) params.get(FlexConsts.SQL);
    }

    public static void setSqlArgs(Map params, Object[] args) {
        params.put(FlexConsts.SQL_ARGS, args);
    }

    public static String getSchemaName(Map params) {
        Object schemaNameObj = params.get(FlexConsts.SCHEMA_NAME);
        return schemaNameObj != null ? schemaNameObj.toString().trim() : null;
    }

    public static String getTableName(Map params) {
        Object tableNameObj = params.get(FlexConsts.TABLE_NAME);
        return tableNameObj != null ? tableNameObj.toString().trim() : null;
    }

    public static String[] getPrimaryKeys(Map params) {
        String primaryKey = (String) params.get(FlexConsts.PRIMARY_KEY);
        if (StringUtil.isBlank(primaryKey)) {
            throw FlexExceptions.wrap(LocalizedFormats.OBJECT_NULL_OR_BLANK, "primaryKey");
        }
        String[] primaryKeys = primaryKey.split(",");
        for (int i = 0; i < primaryKeys.length; i++) {
            primaryKeys[i] = primaryKeys[i].trim();
        }
        return primaryKeys;
    }

    public static Object[] getPrimaryValues(Map params) {
        Object primaryValue = params.get(FlexConsts.PRIMARY_VALUE);
        if (primaryValue == null) {
            return FlexConsts.EMPTY_ARRAY;
        }
        if (primaryValue.getClass().isArray()) {
            return (Object[]) primaryValue;
        } else if (primaryValue instanceof Collection) {
            return ((Collection<?>) primaryValue).toArray();
        } else {
            return new Object[]{primaryValue};
        }
    }

    public static QueryWrapper getQueryWrapper(Map params) {
        Object queryWrapper = params.get(FlexConsts.QUERY);
        FlexAssert.notNull(queryWrapper, "queryWrapper");
        return (QueryWrapper) queryWrapper;
    }

    public static Row getRow(Map params) {
        return (Row) params.get(FlexConsts.ROW);
    }

    public static List<Row> getRows(Map params) {
        return (List<Row>) params.get(FlexConsts.ROWS);
    }

    public static TableInfo getTableInfo(ProviderContext context) {
        return TableInfoFactory.ofMapperClass(context.getMapperType());
    }

    public static TableInfo getTableInfoOfEntityClass(Object entity) {
        return TableInfoFactory.ofEntityClass(entity.getClass());
    }

    public static Object getEntity(Map params) {
        return params.get(FlexConsts.ENTITY);
    }

    public static String getFieldName(Map params) {
        return (String) params.get(FlexConsts.FIELD_NAME);
    }

    public static Object getValue(Map params) {
        return params.get(FlexConsts.VALUE);
    }

    public static List<Object> getEntities(Map params) {
        return (List<Object>) params.get(FlexConsts.ENTITIES);
    }

    public static boolean isIgnoreNulls(Map params) {
        return params.containsKey(FlexConsts.IGNORE_NULLS) && (boolean) params.get(FlexConsts.IGNORE_NULLS);
    }

    public static Configuration getConfiguration(Map params) {
        return (Configuration) params.get("$$configuration");
    }

}
