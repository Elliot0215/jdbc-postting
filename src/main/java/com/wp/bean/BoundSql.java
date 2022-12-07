package com.wp.bean;

import com.wp.utils.ParameterMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Elliot0215
 */
public class BoundSql {
    private String parseSql;
    private List<ParameterMapping> parameterMappingList = new ArrayList<ParameterMapping>();

    public BoundSql(String parseSql, List<ParameterMapping> parameterMappingList) {
        this.parseSql = parseSql;
        this.parameterMappingList = parameterMappingList;
    }

    public String getParseSql() {
        return parseSql;
    }

    public void setParseSql(String parseSql) {
        this.parseSql = parseSql;
    }

    public List<ParameterMapping> getParameterMappingList() {
        return parameterMappingList;
    }

    public void setParameterMappingList(List<ParameterMapping> parameterMappingList) {
        this.parameterMappingList = parameterMappingList;
    }
}
