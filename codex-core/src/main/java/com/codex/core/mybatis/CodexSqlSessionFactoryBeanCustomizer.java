package com.codex.core.mybatis;

import com.mybatisflex.core.mybatis.FlexConfiguration;
import com.mybatisflex.spring.boot.ConfigurationCustomizer;
import com.mybatisflex.spring.boot.SqlSessionFactoryBeanCustomizer;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 实现{@link SqlSessionFactoryBeanCustomizer}接口
 * 修改SqlSessionFactoryBean的Configuration为自己的{@link CodexFlexConfiguration}
 *
 * @author evan guo
 * @since 1.0.0
 */
@Component
public class CodexSqlSessionFactoryBeanCustomizer implements SqlSessionFactoryBeanCustomizer {
    @Override
    public void customize(SqlSessionFactoryBean factory) {
        FlexConfiguration configuration = new CodexFlexConfiguration();
        factory.setConfiguration(configuration);
    }

}
