package com.demo.interceptor;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Properties;

//@Component
@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class}),
        @Signature(method = "query", type = Executor.class, args = {MappedStatement.class,
        Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})})
public class DbInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object object=invocation.proceed();
        return object;
    }

    @Override
    public Object plugin(Object target) {
        Object object=Plugin.wrap(target, this);
        return object;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
