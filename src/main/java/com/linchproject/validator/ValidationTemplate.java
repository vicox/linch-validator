package com.linchproject.validator;

import com.linchproject.validator.parsers.IntegerParser;
import com.linchproject.validator.parsers.StringParser;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Georg Schmidl
 */
public class ValidationTemplate {

    private Class<?> clazz;

    private String[] keys;

    private Set<String> required = new HashSet<String>();

    private Map<Class<?>, Parser> parsers = new HashMap<Class<?>, Parser>() {{
        put(String.class, new StringParser());
        put(Integer.class, new IntegerParser());
    }};

    private Map<String, Set<Validator>> validators = new HashMap<String, Set<Validator>>();

    public Data createEmptyData() {
        Data data = new Data(this);

        if (this.clazz != null) {
            for (Method method: this.clazz.getDeclaredMethods()) {
                if (Reflection.isGetter(method)) {
                    String fieldName = Reflection.getNameFromGetter(method.getName());
                    data.add(fieldName);
                }
            }
        }

        if (this.keys != null) {
            for (String key: this.keys) {
                data.add(key);
            }
        }

        return data;
    }

    public Data createDataFrom(Object object) {
        return createEmptyData().readFrom(object);
    }

    public Data createDataFrom(Map<String, String[]> map) {
        return createEmptyData().readFrom(map);
    }

    public ValidationTemplate setClazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    public ValidationTemplate setKeys(String[] keys) {
        this.keys = keys;
        return this;
    }

    public ValidationTemplate setRequired(String key) {
        this.required.add(key);
        return this;
    }

    public ValidationTemplate addParser(Class<?> type, Parser parser) {
        this.parsers.put(type, parser);
        return this;
    }

    public ValidationTemplate addValidator(String key, Validator validator) {
        if (this.validators.get(key) == null) {
            this.validators.put(key, new HashSet<Validator>());
        }

        this.validators.get(key).add(validator);
        return this;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public boolean isRequired(String key) {
        return this.required.contains(key);
    }

    public <T> Parser<T> getParser(Class<T> type) {
        return parsers.get(type);
    }

    public Set<Validator> getValidators(String key) {
        Set<Validator> validators = this.validators.get(key);
        return validators == null ? Collections.<Validator>emptySet() : validators;
    }
}
