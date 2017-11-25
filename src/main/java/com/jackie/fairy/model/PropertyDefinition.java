package com.jackie.fairy.model;

/**
 * Created by jackie on 17/11/25.
 */
public class PropertyDefinition {
    private String name;
    private String ref;
    private String value;

    public PropertyDefinition(String name, String ref, String value) {
        this.name = name;
        this.ref = ref;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PropertyDefinition{" +
                "name='" + name + '\'' +
                ", ref='" + ref + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
