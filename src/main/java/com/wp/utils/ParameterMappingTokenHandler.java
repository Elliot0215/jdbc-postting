package com.wp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于处理 #{department, mode=OUT, jdbcType=CURSOR, javaType=ResultSet, resultMap=departmentResultMap}格式的入参
 */
public class ParameterMappingTokenHandler implements TokenHandler {

    private List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();

    public ParameterMappingTokenHandler() {

    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }

    /**
     * 处理展位符
     *
     * @param content
     * @return
     */
    @Override
    public String handleToken(String content) {
        //这里对内容进行了分析，并且注册到全局里。
        parameterMappings.add(buildParameterMapping(content));
        //结果返回的是一个？，也就是说#{}类型的参数都会被替换成？
        return "?";
    }

    private ParameterMapping buildParameterMapping(String content) {
        ParameterMapping parameterMapping = new ParameterMapping(content);
        return parameterMapping;
    }

}