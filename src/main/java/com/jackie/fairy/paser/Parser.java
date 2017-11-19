package com.jackie.fairy.paser;

import com.jackie.fairy.model.BeanDefinition;

import java.util.List;

/**
 * Created by jackie on 17/11/19.
 */
public interface Parser {
    List<BeanDefinition> parse(String fileName);
}
