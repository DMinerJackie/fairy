package com.jackie.fairy.paser.impl;

import com.jackie.fairy.model.BeanDefinition;
import com.jackie.fairy.paser.Parser;
import com.jackie.fairy.util.JsonReaderUtil;

import java.util.List;

/**
 * Created by jackie on 17/11/19.
 */
public class JsonParserImpl implements Parser {

    @Override
    public List<BeanDefinition> parse(String fileName) {
        return JsonReaderUtil.readJson(fileName);
    }
}
