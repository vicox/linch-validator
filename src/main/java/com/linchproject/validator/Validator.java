package com.linchproject.validator;

import com.linchproject.validator.parsers.IntegerPropertyParser;
import com.linchproject.validator.parsers.StringPropertyParser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Georg Schmidl
 */
public class Validator {

    private Set<String> required = new HashSet<String>();

    private Map<Class<?>, PropertyParser> parsers = new HashMap<Class<?>, PropertyParser>() {{
        put(String.class, new StringPropertyParser());
        put(Integer.class, new IntegerPropertyParser());
    }};

    private Map<String, Set<PropertyValidator>> validators = new HashMap<String, Set<PropertyValidator>>();

    public Data read(Map<String, String[]> map) {
        Data data = new Data(this);

        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            data.addProperty(entry.getKey(), entry.getValue());
        }

        return data;
    }

    public Data read(Object object) {
        Data data = new Data(this);

        for (Method method: object.getClass().getDeclaredMethods()) {
            if (Reflection.isGetter(method)) {
                String fieldName = Reflection.getNameFromGetter(method.getName());
                Class<?> fieldType = method.getReturnType();

                PropertyParser propertyParser = this.parsers.get(fieldType);
                if (propertyParser == null) {
                    throw new ParserNotFoundException("parser not found for " + fieldType);
                }

                try {
                    Object valueObject = method.invoke(object);
                    String[] values = propertyParser.toStringArray(valueObject);
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

    public Validator setRequired(String propertyName) {
        this.required.add(propertyName);
        return this;
    }

    public Validator addParser(Class<?> propertyType, PropertyParser propertyParser) {
        this.parsers.put(propertyType, propertyParser);
        return this;
    }

    public Validator addValidator(String propertyName, PropertyValidator propertyValidator) {
        if (this.validators.get(propertyName) == null) {
            this.validators.put(propertyName, new HashSet<PropertyValidator>());
        }

        this.validators.get(propertyName).add(propertyValidator);
        return this;
    }

    public Set<String> getRequired() {
        return required;
    }

    public Map<Class<?>, PropertyParser> getParsers() {
        return parsers;
    }

    public Map<String, Set<PropertyValidator>> getValidators() {
        return validators;
    }
}
