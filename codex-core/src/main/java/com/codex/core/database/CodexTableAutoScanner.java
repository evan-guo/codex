package com.codex.core.database;

import cn.hutool.core.util.ModifierUtil;
import cn.hutool.core.util.StrUtil;
import com.codex.annotation.CodexField;
import com.codex.core.database.pojo.TableColumnDefinition;
import com.codex.core.database.pojo.TableDefinition;
import com.codex.core.database.table.TableExecutor;
import com.codex.core.database.table.TableExecutorFactory;
import com.codex.core.model.CodexModel;
import com.codex.core.scan.CodexScanner;
import com.codex.core.util.CodexReflectUtil;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.datasource.DataSourceKey;
import com.mybatisflex.core.datasource.FlexDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 处理数据库表与Entity
 *
 * @author evan guo
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CodexTableAutoScanner {

    private final TableExecutorFactory tableExecutorFactory;
    private final Map<String, List<TableDefinition>> schemaTables = new HashMap<>();

    public void doScan() throws SQLException {
        // 获取所有Codex类，绑定表
        List<CodexModel> codexList = CodexScanner.getCodexModelList();
        for (CodexModel codexModel : codexList) {
            bindTable(codexModel);
        }
        // 根据数据源的不同，分别执行SQL
        FlexDataSource flexDataSource = FlexGlobalConfig.getDefaultConfig().getDataSource();
        for (Map.Entry<String, List<TableDefinition>> schemaEntry : schemaTables.entrySet()) {
            if (StringUtils.hasText(schemaEntry.getKey())) {
                DataSourceKey.use(schemaEntry.getKey());
            }
            try (Connection dataSourceConnection = flexDataSource.getConnection()) {
                String tableSchema = dataSourceConnection.getCatalog();
                String databaseProductName = dataSourceConnection.getMetaData().getDatabaseProductName();
                TableExecutor tableExecutor = tableExecutorFactory.get(databaseProductName);
                for (TableDefinition tableDefinition : schemaEntry.getValue()) {
                    if (tableExecutor.tableExists(tableSchema, tableDefinition.getName())) {
                        List<TableColumnDefinition> tableColumns = tableExecutor.getColumns(tableSchema, tableDefinition.getName());
                        Set<String> columnNameSet = tableColumns.stream().map(TableColumnDefinition::getName).collect(Collectors.toSet());
                        List<TableColumnDefinition> needAddColumns = tableDefinition.getColumns().stream().filter(column -> !columnNameSet.contains(column.getName())).collect(Collectors.toList());
                        tableExecutor.addColumn(needAddColumns);
                    } else {
                        tableExecutor.createTable(tableDefinition);
                    }
                }
            } finally {
                DataSourceKey.clear();
            }
        }
    }

    /**
     * 绑定表，封装字段信息和数据源等
     */
    private void bindTable(CodexModel codexModel) {
        String dataSource = "";
        String tableName;
        // 拿到MybatisFlex的@Table注解
        Table tableAnnotation = codexModel.getClazz().getAnnotation(Table.class);
        if (tableAnnotation != null) {
            tableName = tableAnnotation.value();
            if (StringUtils.hasText(tableAnnotation.dataSource())) {
                dataSource = tableAnnotation.dataSource();
            }
        } else {
            tableName = StrUtil.toUnderlineCase(codexModel.getClazzName());
        }
        String comment = StringUtils.hasText(codexModel.getCodex().desc()) ? codexModel.getCodex().desc() : codexModel.getCodex().name();
        List<TableColumnDefinition> codexFieldDefinitions = getCodexFieldDefinitions(tableName, codexModel, tableAnnotation);
        TableDefinition tableDefinition = TableDefinition.builder()
                .dataSource(dataSource)
                .name(tableName)
                .comment(comment)
                .columns(codexFieldDefinitions)
                .build();
        if (schemaTables.containsKey(dataSource)) {
            schemaTables.get(dataSource).add(tableDefinition);
        } else {
            ArrayList<TableDefinition> arrayList = new ArrayList<>();
            arrayList.add(tableDefinition);
            schemaTables.put(dataSource, arrayList);
        }
    }

    /**
     * 获取CodexField
     *
     * @param codexModel Codex信息
     * @param table      MybatisFlex @Table注解
     * @return 表字段定义
     */
    private List<TableColumnDefinition> getCodexFieldDefinitions(String tableName, CodexModel codexModel, Table table) {
        // 过滤字段
        List<Field> fieldList = CodexReflectUtil.getFields(codexModel.getClazz(), field -> {
            // 静态字段、抽象字段与transient修饰的字段不生成
            boolean hasModifier = ModifierUtil.hasModifier(field, ModifierUtil.ModifierType.ABSTRACT, ModifierUtil.ModifierType.STATIC, ModifierUtil.ModifierType.TRANSIENT);
            // Column注解中指定了ignore为true的也不生成
            boolean ignore = Optional.ofNullable(field.getAnnotation(Column.class)).map(Column::ignore).orElse(false);
            return !hasModifier && !ignore;
        });
        // 转换成表字段定义
        return fieldList.stream().map(field -> {
            Id id = field.getAnnotation(Id.class);
            Column column = field.getAnnotation(Column.class);
            CodexField codexField = field.getAnnotation(CodexField.class);
            // 构建表字段定义
            TableColumnDefinition.TableColumnDefinitionBuilder builder = TableColumnDefinition.builder();
            if (id != null) {
                builder.isPrimaryKey(true);
            }
            if (column != null && StringUtils.hasText(column.value())) {
                builder.name(column.value());
            } else {
                builder.name(table.camelToUnderline() ? StrUtil.toUnderlineCase(field.getName()) : field.getName());
            }
            if (codexField != null) {
                builder.comment(StringUtils.hasText(codexField.desc()) ? codexField.desc() : codexField.title());
            }
            builder.table(tableName)
                    .isNullable(codexField == null || !codexField.notNull())
                    .dataType(TableColumnDefinition.getDataType(field));
            return builder.build();
        }).collect(Collectors.toList());
    }


}
