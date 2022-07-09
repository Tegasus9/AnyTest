package com.tegasus9.anytest.core;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;
/**
 * @author Tegasus9
 * @date 2022/7/9 11:02
 * @description
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),})
public class RerouteToTableInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        Object[] invocationArgs = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) invocationArgs[0];

        Object parameter =  invocationArgs[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);

        String originalSql = boundSql.getSql();
        //System.out.println(" ->>>originalSql: " + originalSql);
        DataTestSqlObject dataTestSqlObject = DataTestContextHolder.getDataTestSqlObject();
        List<String> tableNameSet = dataTestSqlObject.getTableNameSet();
        String newSql = null;
        for (String tableName : tableNameSet) {
            if (originalSql.contains(tableName)) {
                newSql = originalSql.replaceAll(tableName, tableName + dataTestSqlObject.getSuffix());
                log.info("find a table= " + tableName);
                break;
            }
        }
        //System.out.println(" ->>>newSql: " + newSql);
        BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), newSql, boundSql.getParameterMappings(), boundSql.getParameterObject());

        BoundSqlSqlSource sqlSource = new BoundSqlSqlSource(newBoundSql);
        //MappedStatement.Builder builder = new MappedStatement.Builder(mappedStatement.getConfiguration(), mappedStatement.getId(), sqlSource, mappedStatement.getSqlCommandType());
        MappedStatement newMappedStatement = copyFromMappedStatement(mappedStatement,sqlSource);

        invocationArgs[0] = newMappedStatement;


        return invocation.proceed();
    }
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    public static class BoundSqlSqlSource implements SqlSource {
        private final BoundSql boundSql;
        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
