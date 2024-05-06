package com.codex.mapper.flex;

import com.codex.mapper.flex.CodexFlexConfiguration;
import com.mybatisflex.core.mybatis.FlexConfiguration;
import com.mybatisflex.spring.boot.SqlSessionFactoryBeanCustomizer;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.stereotype.Component;

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
