package com.codex.core.database.table;

import com.codex.core.database.pojo.TableDefinition;
import com.codex.core.database.pojo.TableColumnDefinition;
import com.mybatisflex.core.row.Db;

import java.util.List;

/**
 * 数据库表处理器
 *
 * @author evan guo
 */
public interface TableExecutor {

    /**
     * 查询表是否存在
     *
     * @param tableSchema 数据库名
     * @param tableName   表名
     * @return 查询结果
     */
    boolean tableExists(String tableSchema, String tableName);

    /**
     * 创建表
     *
     * @param tableDefinition 表名
     * @return 创建结果
     */
    default boolean createTable(TableDefinition tableDefinition) {
        int insert = Db.insertBySql(getSqlCreateStrings(tableDefinition));
        return insert > 0;
    }

    /**
     * 获取创建表的SQL语句
     *
     * @param tableDefinition 表定义
     * @return SQL语句
     */
    String getSqlCreateStrings(TableDefinition tableDefinition);

    /**
     * 添加字段
     *
     * @param columnDefinitions 表定义列表
     */
    default void addColumn(List<TableColumnDefinition> columnDefinitions) {
        for (TableColumnDefinition columnDefinition : columnDefinitions) {
            addColumn(columnDefinition);
        }
    }

    /**
     * 添加字段
     *
     * @param columnDefinitions 表定义
     * @return 添加结果
     */
    default boolean addColumn(TableColumnDefinition columnDefinitions) {
        int update = Db.updateBySql(getSqlAddColumnStrings(columnDefinitions));
        return update > 0;
    }

    /**
     * 获取添加字段的SQL语句
     *
     * @param tableColumnDefinition 表字段定义
     * @return SQL语句
     */
    String getSqlAddColumnStrings(TableColumnDefinition tableColumnDefinition);

    /**
     * 查询表列
     *
     * @param tableSchema 数据库名
     * @param tableName   表名
     * @return 查询结果
     */
    List<TableColumnDefinition> getColumns(String tableSchema, String tableName);


}
