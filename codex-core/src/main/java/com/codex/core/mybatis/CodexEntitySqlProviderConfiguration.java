package com.codex.core.mybatis;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置CodexEntitySqlProvider，为其注入SqlSessionFactory
 *
 * @author evan guo
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Configuration
public class CodexEntitySqlProviderConfiguration {

    private final SqlSessionFactory sqlSessionFactory;

    @Bean
    public CodexEntitySqlProvider getCodexEntitySqlProvider() {
        return new CodexEntitySqlProvider(sqlSessionFactory.getConfiguration());
    }

}
