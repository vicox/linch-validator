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

    public static String REQUIRED_ERROR = "required";
    public static String PARSER_MISSING_ERROR = "parser.not.found";
    public static String PARSE_ERROR = "invalid.type";

    private ValidationTemplate validationTemplate;

    private Map<String, Value> values = new LinkedHashMap<String, Value>();

    private Map<String, String> errors = new LinkedHashMap<String, String>();

    private boolean validated;

    public Data(ValidationTemplate validationTemplate) {
        this.validationTemplate = validationTemplate;
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
                String type = Reflection.getNameFromGetter(method.getName());

                Value value = this.getValues().get(type);
                if (value != null) {
                    Class<?> fieldType = method.getReturnType();

                    Parser parser = this.getValidationTemplate().getParser(fieldType);
                    if (parser == null) {
                        throw new ParserNotFoundException("parser not found for " + fieldType);
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
        for (Map.Entry<String, Value> entry: this.values.entrySet()) {
            String key = entry.getKey();
            Value value = entry.getValue();
            
            if (this.getValidationTemplate().isRequired(key) && value.isEmpty()) {
                this.errors.put(key, REQUIRED_ERROR);
                continue;
            }

            if (!value.isEmpty()) {
                Class<?> type = getValidationTemplate().getFieldType(key);

                if (!value.isParsed()) {
                    Parser parser = this.getValidationTemplate().getParser(type);
                    if (parser == null) {
                        this.errors.put(key, PARSER_MISSING_ERROR);
                        continue;
                    }

                    try {
                        value.setParsed(parser.parse(value));

                    } catch (ParseException e) {
                        this.errors.put(key, PARSE_ERROR);
                        continue;
                    }
                }
            }

            for (Validator validator : this.getValidationTemplate().getValidators(key)) {
                if (!validator.isValid(value)) {
                    this.errors.put(key, validator.getKey());
                    break;
                }
            }
        }

        this.validated = true;
        return this;
    }

    public void writeTo(Object object, String... keys) {
        List<String> keyList = Arrays.asList(keys);

        for (Method method: object.getClass().getDeclaredMethods()) {
            if (Reflection.isSetter(method)) {
                String fieldName = Reflection.getNameFromSetter(method.getName());

                if (keyList.isEmpty() || keyList.contains(fieldName)) {
                    Value value = this.getValues().get(fieldName);

                    if (value != null) {
                        Object parsed = value.getParsed();

                        try {
                            method.invoke(object, parsed);
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

    public ValidationTemplate getValidationTemplate() {
        return validationTemplate;
    }

    public boolean isValidated() {
        return validated;
    }

    public Map<String, Value> getValues() {
        return values;
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
        Value value = this.values.get(key);
        return value == null ? null : (T) value.getParsed();
    }

    public Map<String, String> getErrors() {
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
