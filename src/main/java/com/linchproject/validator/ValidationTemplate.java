package com.linchproject.validator;

import com.linchproject.validator.parsers.IntegerParser;
import com.linchproject.validator.parsers.StringParser;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Georg Schmidl
 */
public class ValidationTemplate {

    private Map<String, Class<?>> fields = new LinkedHashMap<String, Class<?>>();

    private Set<String> required = new HashSet<String>();

    private Map<Class<?>, Parser> parsers = new HashMap<Class<?>, Parser>() {{
        put(String.class, new StringParser());
        put(Integer.class, new IntegerParser());
    }};

    private Map<String, Set<Validator>> validators = new HashMap<String, Set<Validator>>();

    public Data createEmptyData() {
        Data data = new Data(this);

        if (this.fields != null) {
            for (String key: this.fields.keySet()) {
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

    public ValidationTemplate addField(String key) {
        return this.addFields(key);
    }

    public ValidationTemplate addField(String key, Class<?> type) {
        this.fields.put(key, type);
        return this;
    }

    public ValidationTemplate addFields(String... keys) {
        return addFields(Arrays.asList(keys));
    }

    public ValidationTemplate addFields(Collection<String> keys) {
        for (String key: keys) {
            this.fields.put(key, String.class);
        }
        return this;
    }

    public ValidationTemplate addFields(Map<String, Class<?>> fields) {
        this.fields.putAll(fields);
        return this;
    }

    public ValidationTemplate addFields(Class<?> clazz) {
        for (Method method: clazz.getDeclaredMethods()) {
            if (Reflection.isGetter(method)) {
                String key = Reflection.getNameFromGetter(method.getName());
                Class<?> type = method.getReturnType();
                this.fields.put(key, type);
            }
        }
        return this;
    }


    public Class<?> getFieldType(String key) {
        return this.fields.get(key);
    }

    public ValidationTemplate setRequired(String key) {
        this.required.add(key);
        return this;
    }

    public ValidationTemplate setRequired(String... keys) {
        Collections.addAll(this.required, keys);
        return this;
    }

    public ValidationTemplate setAllRequired() {
        this.required.addAll(fields.keySet());
        return this;
    }

    public ValidationTemplate addParser(Class<?> type, Parser parser) {
        this.parsers.put(type, parser);
        return this;
    }

    public ValidationTemplate addValidator(String key, Validator validator) {
        return this.addValidators(key, validator);
    }

    public ValidationTemplate addValidators(String key, Validator... validators) {
        if (this.validators.get(key) == null) {
            this.validators.put(key, new HashSet<Validator>());
        }
        Collections.addAll(this.validators.get(key), validators);
        return this;
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
