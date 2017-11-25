package com.jackie.fairy.context;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jackie.fairy.constant.ParseType;
import com.jackie.fairy.model.BeanDefinition;
import com.jackie.fairy.model.PropertyDefinition;
import com.jackie.fairy.paser.Parser;
import com.jackie.fairy.paser.factory.ParserFactory;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by jackie on 17/11/19.
 */
public class FairyApplicationContext {
    private static final Logger LOG = LoggerFactory.getLogger(FairyApplicationContext.class);

    private List<BeanDefinition> beanDefinitions = Lists.newArrayList();
    private Map<String, Object> instanceBeans = Maps.newHashMap();

    public FairyApplicationContext() {
    }

    public FairyApplicationContext(String configLocation) {
        this(configLocation, ParseType.XML_PARSER);
    }

    public FairyApplicationContext(String configLocation, ParseType parseType) {
        // 加载xml并转换为BeanDefinition
        this.loadConfigFile(configLocation, parseType);

        // 实例化BeanDefinition
        this.instanceBeanDefinitions();

        this.injectObject();
    }

    private void injectObject() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Object bean = instanceBeans.get(beanDefinition.getId());
            if (bean != null) {
                try {
                    BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
                    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

                    for (PropertyDefinition propertyDefinition : beanDefinition.getPropertyDefinitions()) {
                        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                            if (StringUtils.equals(propertyDescriptor.getName(), propertyDefinition.getName())) {
                                Method setter = propertyDescriptor.getWriteMethod();
                                if (setter != null) {
                                    Object value = null;
                                    if (StringUtils.isNotEmpty(propertyDefinition.getRef())) {
                                        value = instanceBeans.get(propertyDefinition.getRef());
                                    } else {
                                        value = ConvertUtils.convert(propertyDefinition.getValue(), propertyDescriptor.getPropertyType());
                                    }

                                    setter.setAccessible(true);
                                    try {
                                        setter.invoke(bean, value);
                                    } catch (Exception e) {
                                        LOG.error("invoke setter.invoke failed", e);
                                    }
                                }
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    LOG.error("invoke getBean failed", e);
                }
            }
        }
    }

    private void loadConfigFile(String configLocation, ParseType parseType) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(configLocation),
                "configLocation must be not null");

        Parser parser = ParserFactory.getParser(parseType);
        this.beanDefinitions = parser.parse(configLocation);
    }

    private void instanceBeanDefinitions() {
        if (CollectionUtils.isNotEmpty(beanDefinitions)) {
            for (BeanDefinition beanDefinition : beanDefinitions) {
                if (StringUtils.isNotEmpty(beanDefinition.getClassName())) {
                    try {
                        instanceBeans.put(beanDefinition.getId(),
                                Class.forName(beanDefinition.getClassName()).newInstance());
                        LOG.info("instance beans successfully, instanceBeans: {}", instanceBeans);
                    } catch (InstantiationException e) {
                        LOG.error("instantiation failed", e);
                    } catch (IllegalAccessException e) {
                        LOG.error("illegalAccessException", e);
                    } catch (ClassNotFoundException e) {
                        LOG.error("classNotFoundException", e);
                    }
                }
            }
        }
    }

    public Object getBean(String beanName) {
        return instanceBeans.get(beanName);
    }
}
