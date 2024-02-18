package com.codex.core.mybatis;

import com.codex.core.config.CodexConstants;
import com.mybatisflex.core.mybatis.FlexResultSetHandler;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetWrapper;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.Map;

/**
 * 重写MyBatis-Flex的{@link FlexResultSetHandler}，对查询出的Row进行额外处理
 *
 * @author evan guo
 * @since 1.0.0
 */
public class CodexFlexResultSetHandler extends FlexResultSetHandler {

    private final Configuration configuration;
    private final BoundSql boundSql;

    public CodexFlexResultSetHandler(Executor executor, MappedStatement mappedStatement, ParameterHandler parameterHandler, ResultHandler<?> resultHandler, BoundSql boundSql, RowBounds rowBounds) {
        super(executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds);
        this.configuration = mappedStatement.getConfiguration();
        this.boundSql = boundSql;
    }

    @Override
    public void handleRowValues(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler, RowBounds rowBounds, ResultMapping parentMapping) throws SQLException {
        if (resultMap.getType() == Object.class) {
            if (this.boundSql.getParameterObject() instanceof Map) {
                Map parameterObject = (Map) this.boundSql.getParameterObject();
                if (parameterObject.containsKey(CodexConstants.CLASS)) {
                    Class<?> clazz = (Class<?>) parameterObject.get(CodexConstants.CLASS);
                    resultMap = new ResultMap.Builder(configuration, resultMap.getId(), clazz, resultMap.getResultMappings()).build();
                }
            }
        }
        super.handleRowValues(rsw, resultMap, resultHandler, rowBounds, parentMapping);
    }

}
