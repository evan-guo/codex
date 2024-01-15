package com.codex.core.database.table;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.Map;

/**
 * TableExecutor 实现类工厂
 * @author evan guo
 */
@Component
public class TableExecutorFactory {

    private final Map<String, TableExecutor> executorMap;

    public TableExecutorFactory(Map<String, TableExecutor> executorMap) {
        this.executorMap = new LinkedCaseInsensitiveMap<>(executorMap.size());
        this.executorMap.putAll(executorMap);
    }

    public TableExecutor get(String databaseProductName) {
        String key = databaseProductName + "TableExecutor";
        if (executorMap.containsKey(key)) {
            return executorMap.get(key);
        } else {
            return executorMap.get("StandardTableExecutor");
        }
    }

}
