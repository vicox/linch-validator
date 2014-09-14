package com.linchproject.validator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Georg Schmidl
 */
public class Data {

    private Validator validator;

    private Map<String, Value> values = new LinkedHashMap<String, Value>();

    private Map<String, Object> objects = new LinkedHashMap<String, Object>();

    private Map<String, Error> errors = new LinkedHashMap<String, Error>();

    public Data() {
        this(new Validator());
    }

    public Data(Validator validator) {
        for (String key: validator.getFieldKeys()) {
            this.add(key);
        }
        this.validator = validator;
    }

    public Data set(String key, String... strings) {
        Value value = this.getValues().get(key);
        if (value != null) {
            value.setStrings(strings);
        }
        return this;
    }

    public Data readFrom(Map<String, String[]> map) {
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            Value value = this.getValues().get(entry.getKey());
            if (value != null) {
                value.setStrings(entry.getValue());
            }
        }
        return this;
    }

    public Data readFrom(Object object) {
        for (Method method: object.getClass().getDeclaredMethods()) {
            if (Reflection.isGetter(method)) {
                String key = Reflection.getNameFromGetter(method.getName());

                Value value = this.getValues().get(key);
                if (value != null) {
                    Class<?> type = method.getReturnType();

                    Parser parser = this.getValidator().getParser(key);

                    if (parser == null) {
                        parser = this.getValidator().getParser(type);
                    }

                    if (parser == null) {
                        throw new ParserNotFoundException("parser not found for " + type);
                    }

                    try {
                        Object valueObject = method.invoke(object);
                        String[] values = parser.toStringArray(valueObject);
                        value.setStrings(values);

                    } catch (IllegalAccessException e) {
                        // ignore
                    } catch (InvocationTargetException e) {
                        // ignore
                    }
                }
            }
        }

        return this;
    }

    public Data validate() {
        this.validator.validate(this);
        return this;
    }

    public void writeTo(Object bean, String... keys) {
        List<String> keyList = Arrays.asList(keys);

        for (Method method: bean.getClass().getDeclaredMethods()) {
            if (Reflection.isSetter(method)) {
                String fieldName = Reflection.getNameFromSetter(method.getName());

                if (keyList.isEmpty() || keyList.contains(fieldName)) {
                    Object object = this.getObjects().get(fieldName);

                    if (object != null) {
                        try {
                            method.invoke(bean, object);
                        } catch (IllegalAccessException e) {
                            // ignore
                        } catch (InvocationTargetException e) {
                            // ignore
                        }
                    }
                }
            }
        }
    }

    public Validator getValidator() {
        return validator;
    }

    public Map<String, Value> getValues() {
        return values;
    }

    public Map<String, Object> getObjects() {
        return objects;
    }

    public String getString(String key) {
        Value value = this.values.get(key);
        return value == null ? null : value.getString();
    }

    public String[] getStrings(String key) {
        Value value = this.values.get(key);
        return value == null ? null : value.getStrings();
    }

    public <T> T get(String key) {
        return (T) this.objects.get(key);
    }

    public Map<String, Error> getErrors() {
        return errors;
    }

    public boolean isValid() {
        return this.errors.isEmpty();
    }

    public Data add(String key) {
        this.values.put(key, new Value(this));
        return this;
    }

    public Data add(String key, String value) {
        this.values.put(key, new Value(this, value));
        return this;
    }

    public Data add(String key, String... values) {
        this.values.put(key, new Value(this, values));
        return this;
    }

    public Data remove(String key) {
        this.values.remove(key);
        return this;
    }
}
