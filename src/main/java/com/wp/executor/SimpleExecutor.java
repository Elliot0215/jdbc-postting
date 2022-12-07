package com.wp.executor;

import cn.hutool.core.util.StrUtil;
import com.wp.bean.BoundSql;
import com.wp.bean.Configuration;
import com.wp.bean.MapperStatement;
import com.wp.utils.GenericTokenParser;
import com.wp.utils.ParameterMapping;
import com.wp.utils.ParameterMappingTokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单执行器实现类
 * @author Elliot0215
 */
public class SimpleExecutor implements Executor {
    @Override
    public <E> List<E> query(Configuration configuration, MapperStatement mapperStatement, Object... param) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException {
        //1.获取数据库连接
        Connection connection = configuration.getDataSource().getConnection();
        //2.解析sql和参数名称
        String sql = mapperStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getParseSql());

        //3.反射处理参数
        String paramterType = mapperStatement.getParamterType();
        Class<?> paramterClazzType = null;
        if (StrUtil.isNotBlank(paramterType)) {
            paramterClazzType = getClazzType(paramterType);
            List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
            for (int i = 0; i < parameterMappingList.size(); i++) {
                ParameterMapping parameterMapping = parameterMappingList.get(i);
                String content = parameterMapping.getContent();
                Field declaredField = paramterClazzType.getDeclaredField(content);
                declaredField.setAccessible(true);
                Object o = declaredField.get(param[0]);
                preparedStatement.setObject(i + 1, o);
            }
        }

        ResultSet resultSet = preparedStatement.executeQuery();

        //4.反射封装结果集
        String resultType = mapperStatement.getResultType();
        Class<?> resultClazzType = getClazzType(resultType);
        ArrayList<E> es = new ArrayList<>();
        while (resultSet.next()) {
            Object o = resultClazzType.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                //获取数据库字段名称
                String columnName = metaData.getColumnName(i);
                //获取字段的值
                Object object = resultSet.getObject(columnName);
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultClazzType);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, object);
            }
            es.add((E) o);
        }

        return es;
    }

    private Class<?> getClazzType(String paramterType) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(paramterType);
        return aClass;
    }

    /**
     * 解析sql  select * from sys_user where id = #{id} and username = #{username}
     *
     * @param sql
     * @return BoundSql
     */
    private BoundSql getBoundSql(String sql) {
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String parseSql = genericTokenParser.parse(sql);
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        BoundSql boundSql = new BoundSql(parseSql, parameterMappings);
        return boundSql;
    }
}
