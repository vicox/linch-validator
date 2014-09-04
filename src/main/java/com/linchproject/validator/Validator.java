package com.linchproject.validator;

import com.linchproject.validator.parser.IntegerParser;
import com.linchproject.validator.parser.StringParser;

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

    private Map<Class<?>, Parser> parsers = new HashMap<Class<?>, Parser>() {{
        put(String.class, new StringParser());
        put(Integer.class, new IntegerParser());
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
            if (required.contains(property.getName()) && property.getStringValue().isEmpty()) {
                errors.put(property.getName(), REQUIRED_ERROR);
                break;
            }

            Class<?> propertyClass = getFieldClass(clazz, property.getName());
            if (propertyClass == null) {
                propertyClass = String.class;
            }

            // parser missing errors
            Parser parser = this.parsers.get(propertyClass);
            if (parser == null) {
                errors.put(property.getName(), PARSER_MISSING_ERROR);
                break;
            }

            // parse errors
            try {
                property.parse(parser);
            } catch (ParseException e) {
                errors.put(property.getName(), PARSE_ERROR);
                break;
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

    public Validator addParser(Class<?> propertyType, Parser parser) {
        this.parsers.put(propertyType, parser);
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

    public void write(Data data, Object object) {
        Class<?> objectClass = object.getClass();

        for (Method method: objectClass.getDeclaredMethods()) {
            if (isSetter(method)) {

                String fieldName = getNameFromSetter(method.getName());
                Class<?> fieldType = method.getParameterTypes()[0];

                Property property = data.get(fieldName);

                if (property != null) {
                    try {
                        Method setter = objectClass.getMethod(getSetterName(fieldName), fieldType);

                        if (!property.isParsed()) {
                            Parser parser = this.parsers.get(fieldType);
                            if (parser == null) {
                                throw new ParserNotFoundException("parser not found for " + fieldType);
                            }
                            property.parse(parser);
                        }

                        Object parsedValue = property.getParsedValue();

                        try {
                            setter.invoke(object, parsedValue);
                        } catch (IllegalAccessException e) {
                            // ignore
                        } catch (InvocationTargetException e) {
                            // ignore
                        }

                    } catch (NoSuchMethodException e) {
                        // ignore
                    }
                }
            }
        }
    }

    private Class<?> getFieldClass(Class<?> clazz, String fieldName) {
        Class<?> fieldClass = null;

        try {
            Method getter = clazz.getMethod(getGetterName(fieldName));
            fieldClass = getter.getReturnType();
        } catch (NoSuchMethodException e) {
            // ignore
        }

        return fieldClass;
    }

    private static String getSetterName(String name) {
        String setterName = "set" + name.substring(0, 1).toUpperCase();
        if (name.length() > 1) {
            setterName += name.substring(1, name.length());
        }
        return  setterName;
    }

    private static String getGetterName(String name) {
        String getterName = "get" + name.substring(0, 1).toUpperCase();
        if (name.length() > 1) {
            getterName += name.substring(1, name.length());
        }
        return  getterName;
    }

    private boolean isSetter(Method method) {
        return method.getName().startsWith("set")
                && method.getName().length() > 3
                && method.getParameterTypes().length > 0;
    }

    private static String getNameFromSetter(String setterName) {
        String name = setterName.substring(3, 4).toLowerCase();
        if (setterName.length() > 5) {
            name += setterName.substring(5, setterName.length());
        }
        return name;
    }
}
