package com.wp.sqlSession;


import cn.hutool.core.collection.CollUtil;
import com.wp.bean.Configuration;
import com.wp.bean.MapperStatement;
import com.wp.executor.Executor;
import com.wp.executor.SimpleExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Elliot0215
 */
public class DefaultSqlSession implements SqlSession {
    private static final Logger log = LoggerFactory.getLogger(DefaultSqlSession.class);
    private Configuration configuration;
    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... param) throws SQLException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException, IntrospectionException, InvocationTargetException, InstantiationException {
        log.info("用户传递参数：{}",param);
        Executor executor = new SimpleExecutor();
        MapperStatement mapperStatement = configuration.getMapperStatementMap().get(statementId);
        return executor.query(configuration,mapperStatement,param);
    }

    @Override
    public <T> T selectOne(String statementId, Object... param) throws SQLException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException, IntrospectionException, InvocationTargetException, InstantiationException {
        List<Object> objectList = this.selectList(statementId, param);
        if (CollUtil.isEmpty(objectList)){
            return null;
        }
        if (objectList.size() == 1) {
            return (T) objectList.get(0);
        } else {
            throw new RuntimeException("返回结果大于1");
        }
    }
}
