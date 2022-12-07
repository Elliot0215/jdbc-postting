package com.wp.xml;

import com.wp.bean.Configuration;
import com.wp.bean.MapperStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * mapper解析
 * @author Elliot0215
 */
public class XMLMapperBuilder {
    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parseMapper(InputStream inputStream) throws DocumentException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        List<Element> selectNodes = rootElement.selectNodes("//select");
        for (Element selectNode : selectNodes) {
            String id = selectNode.attributeValue("id");
            String resultType = selectNode.attributeValue("resultType");
            String paramterType = selectNode.attributeValue("paramterType");
            String sql = selectNode.getTextTrim();
            MapperStatement mapperStatement = new MapperStatement();
            mapperStatement.setId(id);
            mapperStatement.setResultType(resultType);
            mapperStatement.setParamterType(paramterType);
            mapperStatement.setSql(sql);
            String namespace = rootElement.attributeValue("namespace");
            String key = namespace + "." + id;
            configuration.getMapperStatementMap().put(key, mapperStatement);
        }

    }
}
