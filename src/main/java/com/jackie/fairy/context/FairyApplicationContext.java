package com.jackie.fairy.context;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jackie.fairy.annotation.JackieAutowired;
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
import java.lang.reflect.Field;
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

        // 基于注解的依赖注入
        this.annotationInject();

        // 实现依赖注入
        this.injectObject();
    }

    private void annotationInject() {
        for (String beanName : instanceBeans.keySet()) {
            Object bean = instanceBeans.get(beanName);
            if (bean != null) {
                try {
                    BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
                    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

                    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                        // 获取Setter方法
                        Method setter = propertyDescriptor.getWriteMethod();

                        if (setter != null && setter.isAnnotationPresent(JackieAutowired.class)) {
                            JackieAutowired jackieAutowired = setter.getAnnotation(JackieAutowired.class);
                            Object value = null;

                            if (jackieAutowired != null && StringUtils.isNotEmpty(jackieAutowired.name())) {
                                value = instanceBeans.get(jackieAutowired.name());
                            } else {
                                value = instanceBeans.get(propertyDescriptor.getName());
                                if (value == null) {
                                    for (String key : instanceBeans.keySet()) {
                                        if (propertyDescriptor.getPropertyType().isAssignableFrom(instanceBeans.get(key).getClass())) {
                                            value=instanceBeans.get(key);//类型匹配的话就把此相同类型的
                                            break;//找到了类型相同的bean，退出循环
                                        }
                                    }
                                }
                            }

                            setter.setAccessible(true);
                            try {
                                setter.invoke(bean, value);
                            } catch (Exception e) {
                                LOG.error("invoke setter invoke failed", e);
                            }
                        }
                    }

                    Field[] fields = bean.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(JackieAutowired.class)) {
                            JackieAutowired jackieAutowired = field.getAnnotation(JackieAutowired.class);
                            Object value = null;

                            if (jackieAutowired != null && StringUtils.isNotEmpty(jackieAutowired.name())) {
                                value = instanceBeans.get(jackieAutowired.name());
                            } else {
                                value = instanceBeans.get(field.getName());
                                if (value == null) {
                                    for (String key : instanceBeans.keySet()) {
                                        if (field.getType().isAssignableFrom(instanceBeans.get(key).getClass())) {
                                            value = instanceBeans.get(key);
                                            break;
                                        }
                                    }
                                }
                            }

                            field.setAccessible(true);
                            try {
                                field.set(bean, value);
                            } catch (Exception e) {
                                LOG.error("invoke field.set failed", e);
                            }
                        }
                    }
                } catch (Exception e) {
                    LOG.error("invoke getBean failed", e);
                }
            }
        }
    }

    private void injectObject() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Object bean = instanceBeans.get(beanDefinition.getId());
            if (bean != null) {
                try {
                    BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
                    /**
                     * 通过BeanInfo来获取属性的描述器(PropertyDescriptor)
                     * 通过这个属性描述器就可以获取某个属性对应的getter/setter方法
                     * 然后我们就可以通过反射机制来调用这些方法。
                     */
                    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

                    for (PropertyDefinition propertyDefinition : beanDefinition.getPropertyDefinitions()) {
                        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                            // 用户定义的bean属性与java内省后的bean属性名称相同时
                            if (StringUtils.equals(propertyDescriptor.getName(), propertyDefinition.getName())) {
                                // 获取setter方法
                                Method setter = propertyDescriptor.getWriteMethod();
                                if (setter != null) {
                                    Object value = null;
                                    if (StringUtils.isNotEmpty(propertyDefinition.getRef())) {
                                        // 根据bean的名称在instanceBeans中获取指定的对象值
                                        value = instanceBeans.get(propertyDefinition.getRef());
                                    } else {
                                        value = ConvertUtils.convert(propertyDefinition.getValue(), propertyDescriptor.getPropertyType());
                                    }

                                    // //保证setter方法可以访问私有
                                    setter.setAccessible(true);
                                    try {
                                        // 把引用对象注入到属性
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
