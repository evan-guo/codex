package com.codex.core.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author evan guo
 */
@Data
@Component
@ConfigurationProperties(prefix = "codex")
public class CodexProperties {

    /**
     * 启用Codex
     */
    private boolean enable = true;

    /**
     * 数据库配置
     */
    private DbConfig db;

    @Setter
    @Getter
    public static class DbConfig {
        /**
         * 自动建表
         */
        private boolean ddlAuto = false;

    }


}
