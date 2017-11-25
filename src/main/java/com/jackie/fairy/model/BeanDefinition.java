package com.jackie.fairy.model;

import java.util.List;

/**
 * Created by jackie on 17/11/19.
 */
public class BeanDefinition {
    private String id;
    private String className;
    private List<PropertyDefinition> propertyDefinitions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public BeanDefinition(String id, String className) {
        this.id = id;
        this.className = className;
    }

    public List<PropertyDefinition> getPropertyDefinitions() {
        return propertyDefinitions;
    }

    public void setPropertyDefinitions(List<PropertyDefinition> propertyDefinitions) {
        this.propertyDefinitions = propertyDefinitions;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "id='" + id + '\'' +
                ", className='" + className + '\'' +
                ", propertyDefinitions=" + propertyDefinitions +
                '}';
    }
}
