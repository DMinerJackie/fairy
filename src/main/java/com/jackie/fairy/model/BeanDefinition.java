package com.jackie.fairy.model;

/**
 * Created by jackie on 17/11/19.
 */
public class BeanDefinition {
    private String id;
    private String className;

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

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "id='" + id + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
