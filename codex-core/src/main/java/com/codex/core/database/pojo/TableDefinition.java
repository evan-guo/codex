package com.codex.core.database.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表结构
 *
 * @author evan guo
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TableDefinition implements Serializable {

    private String dataSource = "";

    private String schema = "";

    private String name;

    private String primaryKey;

    private List<TableColumnDefinition> columns = new ArrayList<>();

    private String comment;

}