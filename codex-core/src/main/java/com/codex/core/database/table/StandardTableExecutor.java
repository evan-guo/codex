package com.codex.core.database.table;

import com.codex.core.database.pojo.TableDefinition;
import com.codex.core.database.pojo.TableColumnDefinition;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 标准数据库表执行器
 *
 * @author evan guo
 */
@Component
public class StandardTableExecutor implements TableExecutor {

    @Override
    public boolean tableExists(String tableSchema, String tableName) {
        return Db.selectCount("select count(1) from information_schema.tables where table_schema = ? and table_name = ? ", tableSchema, tableName) > 0;
    }

    @Override
    public String getSqlCreateStrings(TableDefinition tableDefinition) {
        Assert.notNull(tableDefinition, "tableDefinition can not be null");
        String tableName = tableDefinition.getName();
        StringBuilder buf = new StringBuilder().append("create table ").append(tableName).append(" ( ");
        boolean isFirst = true;
        for (TableColumnDefinition column : tableDefinition.getColumns()) {
            if (isFirst) {
                isFirst = false;
            } else {
                buf.append(", ");
            }
            buf.append(column.getName()).append(' ').append(column.getDataType());
            getColumnDefinitionSql(column, buf);
        }
        buf.append(')');
        if (tableDefinition.getComment() != null) {
            buf.append(" comment = '").append(tableDefinition.getComment()).append("'");
        }
        return buf.toString();
    }

    @Override
    public String getSqlAddColumnStrings(TableColumnDefinition columnDefinition) {
        Assert.notNull(columnDefinition, "columnDefinition can not be null");
        StringBuilder buf = new StringBuilder().append("alter table ").append(columnDefinition.getTable());
        buf.append(" add ").append(columnDefinition.getName()).append(' ').append(columnDefinition.getDataType());
        getColumnDefinitionSql(columnDefinition, buf);
        return buf.toString();
    }


    @Override
    public List<TableColumnDefinition> getColumns(String tableSchema, String tableName) {
        Assert.hasText(tableSchema, "tableSchema can not be null");
        Assert.hasText(tableName, "tableName can not be null");
        List<Row> rows = Db.selectListBySql("select * from information_schema.columns where table_schema = ? and table_name = ?", tableSchema, tableName);
        return rows.stream().map(row -> TableColumnDefinition.builder()
                .table(row.getString("TABLE_NAME"))
                .name(row.getString("COLUMN_NAME"))
                .isNullable("YES".equals(row.getString("IS_NULLABLE")))
                .dataType(row.getString("COLUMN_TYPE"))
                .comment(row.getString("COLUMN_COMMENT"))
                .defaultValue(row.getString("COLUMN_DEFAULT"))
                .isPrimaryKey("PRI".equals(row.getString("COLUMN_KEY")))
                .build()
        ).collect(Collectors.toList());
    }

    public String getSqlDropStrings(TableDefinition tableDefinition) {
        return "drop table if exists " + tableDefinition.getName();
    }

    private void getColumnDefinitionSql(TableColumnDefinition columnDefinition, StringBuilder buf) {
        if (columnDefinition.isPrimaryKey()) {
            buf.append(' ').append("primary key");
        } else {
            if (StringUtils.hasText(columnDefinition.getDefaultValue())) {
                buf.append(" default ").append(columnDefinition.getDefaultValue());
            }
            if (!columnDefinition.isNullable()) {
                buf.append(" not null");
            }
            if (StringUtils.hasText(columnDefinition.getComment())) {
                buf.append(" comment '").append(columnDefinition.getComment()).append("'");
            }
        }
    }

}
