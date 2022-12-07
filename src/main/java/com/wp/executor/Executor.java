package com.wp.executor;

import com.wp.bean.Configuration;
import com.wp.bean.MapperStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Elliot0215
 */
public interface Executor {
    <E> List<E> query(Configuration configuration, MapperStatement mapperStatement, Object... param) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException;
}
