package com.jackie.fairy.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.jackie.fairy.model.BeanDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jackie on 17/11/20.
 */
public class JsonReaderUtil {
    private static final Logger LOG = LoggerFactory.getLogger(JsonReaderUtil.class);

    public static List<BeanDefinition> readJson(String fileName) {
        Gson gson = new Gson();
        Map<String, String> elements = Maps.newHashMap();

        URL fileUrl = FileURLUtil.getFileURL(fileName);
        try {
            elements = gson.fromJson(new FileReader(fileUrl.getPath()), HashMap.class);
        } catch (FileNotFoundException e) {
            LOG.error("fileNotFoundException", e);
        }

        List<BeanDefinition> beanDefinitions = Lists.newArrayList();
        for (Map.Entry<String, String> entry : elements.entrySet()) {
            beanDefinitions.add(new BeanDefinition(entry.getKey(), entry.getValue()));
        }

        return beanDefinitions;
    }
}
