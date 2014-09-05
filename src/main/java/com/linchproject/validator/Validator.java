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

    public static String REQUIRED_ERROR = "required";
    public static String PARSER_MISSING_ERROR = "parser.not.found";
    public static String PARSE_ERROR = "invalid.type";

    private Set<String> required = new HashSet<String>();

    private Map<Class<?>, PropertyParser> parsers = new HashMap<Class<?>, PropertyParser>() {{
        put(String.class, new StringPropertyParser());
        put(Integer.class, new IntegerPropertyParser());
    }};

    private Map<String, Set<PropertyValidator>> validators = new HashMap<String, Set<PropertyValidator>>();

    public Map<String, String> validate(Data data, Class<?> clazz) {
        Map<String, String> errors = new LinkedHashMap<String, String>();

        // required errors for properties that are not in data
        for (String requiredPropertyName : this.required) {
            Property property = data.get(requiredPropertyName);
            if (property == null) {
                errors.put(requiredPropertyName, REQUIRED_ERROR);
            }
        }

        for (Property property : data.values()) {

            // required errors for properties that are empty
            if (required.contains(property.getName()) && property.isEmpty()) {
                errors.put(property.getName(), REQUIRED_ERROR);
                break;
            }

            Class<?> propertyClass = Reflection.getFieldClass(clazz, property.getName());
            if (propertyClass == null) {
                propertyClass = String.class;
            }

            if (!property.isParsed()) {

                // parser missing errors
                PropertyParser propertyParser = this.parsers.get(propertyClass);
                if (propertyParser == null) {
                    errors.put(property.getName(), PARSER_MISSING_ERROR);
                    break;
                }

                // parse errors
                try {
                    property.parse(propertyParser);
                } catch (ParseException e) {
                    errors.put(property.getName(), PARSE_ERROR);
                    break;
                }
            }

            // validation errors
            Set<PropertyValidator> propertyValidators = this.validators.get(property.getName());
            if (propertyValidators != null) {
                for (PropertyValidator propertyValidator : propertyValidators) {
                    if (!propertyValidator.isValid(property)) {
                        errors.put(property.getName(), propertyValidator.getKey());
                        break;
                    }
                }
            }
        }

        return errors;
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

    public Data read(Map<String, String[]> map) {
        Data data = new Data();

        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            data.addProperty(entry.getKey(), entry.getValue());
        }

        return data;
    }

    public Data read(Object object) {
        Data data = new Data();

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
                    String[] stringValues = propertyParser.toStringArray(valueObject);
                    data.addProperty(fieldName, stringValues);

                } catch (IllegalAccessException e) {
                    // ignore
                } catch (InvocationTargetException e) {
                    // ignore
                }
            }
        }

        return data;
    }

    public void write(Data data, Object object) {
        for (Method method: object.getClass().getDeclaredMethods()) {
            if (Reflection.isSetter(method)) {
                String fieldName = Reflection.getNameFromSetter(method.getName());
                Class<?> fieldType = method.getParameterTypes()[0];

                Property property = data.get(fieldName);

                if (property != null) {
                    if (!property.isParsed()) {
                        PropertyParser propertyParser = this.parsers.get(fieldType);
                        if (propertyParser == null) {
                            throw new ParserNotFoundException("parsers not found for " + fieldType);
                        }
                        property.parse(propertyParser);
                    }

                    Object parsedValue = property.getParsedValue();

                    try {
                        method.invoke(object, parsedValue);
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
