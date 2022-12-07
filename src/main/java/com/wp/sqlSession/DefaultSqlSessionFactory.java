package com.wp.sqlSession;


import com.wp.bean.Configuration;

/**
 * @author Elliot0215
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory{

    private Configuration configuration;
    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public SqlSession openSession() {

        return new DefaultSqlSession(configuration);
    }
}
