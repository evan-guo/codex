package com.codex.core.database.pojo;


import lombok.*;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * @author evan guo
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TableColumnDefinition {

    public static final int DEFAULT_LENGTH = 255;

    public static final int DEFAULT_PRECISION = 19;

    public static final int DEFAULT_SCALE = 2;

    private int length = 255;

    private int precision = 19;

    private int scale = 2;

    private String table;

    private String name;

    private boolean isNullable = true;

    private String dataType;

    private String comment;

    private String defaultValue;

    private boolean unique;

    private boolean isPrimaryKey = false;

    public static String getDataType(Field field) {
        String columnType = null;
        if (field.getType() == String.class) {
            columnType = "varchar(255)";
        } else if (field.getType() == Integer.class || field.getType() == Long.class) {
            columnType = "int";
        } else if (field.getType() == Boolean.class) {
            columnType = "tinyint(1)";
        } else if (field.getType() == Date.class) {
            columnType = "datetime";
        } else if (field.getType() == Double.class || field.getType() == Float.class) {
            columnType = "int";
        } else {
            columnType = "varchar(255)";
        }
        return columnType;
    }

}
