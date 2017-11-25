package com.jackie.fairy.util;

import com.google.common.collect.Lists;
import com.jackie.fairy.constant.Constants;
import com.jackie.fairy.model.BeanDefinition;
import com.jackie.fairy.model.PropertyDefinition;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jackie on 17/11/19.
 */
public class XmlReaderUtil {
    private static final Logger LOG = LoggerFactory.getLogger(XmlReaderUtil.class);

    public static List<BeanDefinition> readXml(String fileName) {
        List<BeanDefinition> beanDefinitions = Lists.newArrayList();
        List<PropertyDefinition> propertyDefinitions = Lists.newArrayList();

        //创建一个读取器
        SAXReader saxReader = new SAXReader();
        Document document = null;

        try {
            //获取要读取的配置文件的路径
            URL xmlPath = FileURLUtil.getFileURL(fileName);
            //读取文件内容
            document = saxReader.read(xmlPath);
            //获取xml中的根元素
            Element rootElement = document.getRootElement();

            for (Iterator iterator = rootElement.elementIterator(); iterator.hasNext(); ) {
                Element element = (Element)iterator.next();
                String id = element.attributeValue(Constants.BEAN_ID_NAME);
                String clazz = element.attributeValue(Constants.BEAN_CLASS_NAME);
                BeanDefinition beanDefinition = new BeanDefinition(id, clazz);

                for (Iterator propertyIterator = element.elementIterator(); propertyIterator.hasNext();) {
                    Element propertyElement = (Element) propertyIterator.next();
                    String name = propertyElement.attributeValue(Constants.PROPERTY_NAME_NAME);
                    String ref = propertyElement.attributeValue(Constants.PROPERTY_REF_NAME);
                    String value = propertyElement.attributeValue(Constants.PROPERTY_VALUE_NAME);
                    propertyDefinitions.add(new PropertyDefinition(name, ref, value));
                }

                beanDefinition.setPropertyDefinitions(propertyDefinitions);
                beanDefinitions.add(beanDefinition);
                propertyDefinitions = Lists.newArrayList();
            }

        } catch (Exception e) {
            LOG.error("read xml failed", e);
        }

        return beanDefinitions;
    }
}
