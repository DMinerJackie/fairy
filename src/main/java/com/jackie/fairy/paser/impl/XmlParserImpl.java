package com.jackie.fairy.paser.impl;

import com.jackie.fairy.model.BeanDefinition;
import com.jackie.fairy.paser.Parser;
import com.jackie.fairy.util.XmlReaderUtil;

import java.util.List;

/**
 * Created by jackie on 17/11/19.
 */
public class XmlParserImpl implements Parser {

    @Override
    public List<BeanDefinition> parse(String fileName) {
        return XmlReaderUtil.readXml(fileName);
    }
}
