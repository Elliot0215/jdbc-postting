package com.wp.sqlSession;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Elliot0215
 */
public interface SqlSession {
    <E>List<E> selectList(String statementId,Object... param) throws SQLException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException, IntrospectionException, InvocationTargetException, InstantiationException;
    <T> T selectOne(String statementId,Object... param) throws SQLException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException, IntrospectionException, InvocationTargetException, InstantiationException;
}
