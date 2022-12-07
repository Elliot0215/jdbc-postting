package com.wp.bean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Elliot0215
 */
public class Configuration {
    private DataSource dataSource;
    private Map<String, MapperStatement> mapperStatementMap = new HashMap<String, MapperStatement>();

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MapperStatement> getMapperStatementMap() {
        return mapperStatementMap;
    }

    public void setMapperStatementMap(Map<String, MapperStatement> mapperStatementMap) {
        this.mapperStatementMap = mapperStatementMap;
    }
}
