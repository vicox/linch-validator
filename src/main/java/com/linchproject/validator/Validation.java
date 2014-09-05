package com.linchproject.validator;

import com.linchproject.validator.parsers.IntegerParser;
import com.linchproject.validator.parsers.StringParser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Georg Schmidl
 */
public class Validation {

    private Set<String> required = new HashSet<String>();

    private Map<Class<?>, Parser> parsers = new HashMap<Class<?>, Parser>() {{
        put(String.class, new StringParser());
        put(Integer.class, new IntegerParser());
    }};

    private Map<String, Set<Validator>> validators = new HashMap<String, Set<Validator>>();

    public Data create(Map<String, String[]> map) {
        Data data = new Data(this);

        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            data.addProperty(entry.getKey(), entry.getValue());
        }

        return data;
    }

    public Data create(Object object) {
        Data data = new Data(this);

        for (Method method: object.getClass().getDeclaredMethods()) {
            if (Reflection.isGetter(method)) {
                String fieldName = Reflection.getNameFromGetter(method.getName());
                Class<?> fieldType = method.getReturnType();

                Parser parser = this.parsers.get(fieldType);
                if (parser == null) {
                    throw new ParserNotFoundException("parser not found for " + fieldType);
                }

                try {
                    Object valueObject = method.invoke(object);
                    String[] values = parser.toStringArray(valueObject);
                    data.addProperty(fieldName, values);

                } catch (IllegalAccessException e) {
                    // ignore
                } catch (InvocationTargetException e) {
                    // ignore
                }
            }
        }

        return data;
    }

    public Data create(Class<?> clazz) {
        Data data = new Data(this);

        for (Method method: clazz.getDeclaredMethods()) {
            if (Reflection.isGetter(method)) {
                String fieldName = Reflection.getNameFromGetter(method.getName());
                data.addProperty(fieldName);
            }
        }

        return data;
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
