package com.linchproject.validator;

import com.linchproject.validator.parsers.IntegerParser;
import com.linchproject.validator.parsers.StringParser;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Georg Schmidl
 */
public class Validation {

    private Class<?> clazz;

    private String[] properties;

    private Set<String> required = new HashSet<String>();

    private Map<Class<?>, Parser> parsers = new HashMap<Class<?>, Parser>() {{
        put(String.class, new StringParser());
        put(Integer.class, new IntegerParser());
    }};

    private Map<String, Set<Validator>> validators = new HashMap<String, Set<Validator>>();

    public Data create() {
        Data data = new Data(this);

        if (this.clazz != null) {
            for (Method method: this.clazz.getDeclaredMethods()) {
                if (Reflection.isGetter(method)) {
                    String fieldName = Reflection.getNameFromGetter(method.getName());
                    data.addProperty(fieldName);
                }
            }
        }

        if (this.properties != null) {
            for (String property: this.properties) {
                data.addProperty(property);
            }
        }

        return data;
    }

    public Data create(Object object) {
        return create().readFrom(object);
    }

    public Data create(Map<String, String[]> map) {
        return create().readFrom(map);
    }

    public Validation setClazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    public Validation setProperties(String[] properties) {
        this.properties = properties;
        return this;
    }

    public Validation setRequired(String propertyName) {
        this.required.add(propertyName);
        return this;
    }

    public Validation addParser(Class<?> propertyType, Parser parser) {
        this.parsers.put(propertyType, parser);
        return this;
    }

    public Validation addValidator(String propertyName, Validator validator) {
        if (this.validators.get(propertyName) == null) {
            this.validators.put(propertyName, new HashSet<Validator>());
        }

        this.validators.get(propertyName).add(validator);
        return this;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Set<String> getRequired() {
        return required;
    }

    public Map<Class<?>, Parser> getParsers() {
        return parsers;
    }

    public Map<String, Set<Validator>> getValidators() {
        return validators;
    }
}
