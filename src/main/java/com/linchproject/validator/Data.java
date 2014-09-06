package com.linchproject.validator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Georg Schmidl
 */
public class Data {

    public static String REQUIRED_ERROR = "required";
    public static String PARSER_MISSING_ERROR = "parser.not.found";
    public static String PARSE_ERROR = "invalid.type";

    private Template template;

    private Map<String, Value> values = new LinkedHashMap<String, Value>();

    private Map<String, String> errors = new LinkedHashMap<String, String>();

    private boolean validated;

    public Data(Template template) {
        this.template = template;
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
                String fieldName = Reflection.getNameFromGetter(method.getName());

                Value value = this.getValues().get(fieldName);
                if (value != null) {
                    Class<?> fieldType = method.getReturnType();

                    Parser parser = this.getTemplate().getParsers().get(fieldType);
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
            
            if (this.getTemplate().getRequired().contains(key) && value.isEmpty()) {
                this.errors.put(key, REQUIRED_ERROR);
                continue;
            }

            if (!value.isEmpty()) {
                Class<?> type = Reflection.getFieldClass(getTemplate().getClazz(), key);
                if (type == null) {
                    type = String.class;
                }

                if (!value.isParsed()) {
                    Parser parser = this.getTemplate().getParsers().get(type);
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

            Set<Validator> validators = this.getTemplate().getValidators().get(key);
            if (validators != null) {
                for (Validator validator : validators) {
                    if (!validator.isValid(value)) {
                        this.errors.put(key, validator.getKey());
                        break;
                    }
                }
            }
        }

        this.validated = true;
        return this;
    }

    public void writeTo(Object object) {
        for (Method method: object.getClass().getDeclaredMethods()) {
            if (Reflection.isSetter(method)) {
                String fieldName = Reflection.getNameFromSetter(method.getName());

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

    public Template getTemplate() {
        return template;
    }

    public boolean isValidated() {
        return validated;
    }

    public Map<String, Value> getValues() {
        return values;
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

    public Data add(String key, String[] values) {
        this.values.put(key, new Value(this, values));
        return this;
    }

    public Data remove(String key) {
        this.values.remove(key);
        return this;
    }
}
