package com.jackie.fairy.util;

import com.google.common.collect.Lists;
import com.jackie.fairy.model.BeanDefinition;
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
                String id = element.attributeValue("id");
                String clazz = element.attributeValue("class");
                BeanDefinition beanDefinition = new BeanDefinition(id, clazz);
                beanDefinitions.add(beanDefinition);
            }

        } catch (Exception e) {
            LOG.error("read xml failed", e);
        }

        return beanDefinitions;
    }
}
