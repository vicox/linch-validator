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

    private Map<String, Property> properties = new LinkedHashMap<String, Property>();

    private Map<String, String> errors = new LinkedHashMap<String, String>();

    private boolean validated;

    public Data(Template template) {
        this.template = template;
    }

    public Data readFrom(Map<String, String[]> map) {
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            Property property = this.getProperties().get(entry.getKey());
            if (property != null) {
                property.setValues(entry.getValue());
            }
        }

        return this;
    }

    public Data readFrom(Object object) {
        for (Method method: object.getClass().getDeclaredMethods()) {
            if (Reflection.isGetter(method)) {
                String fieldName = Reflection.getNameFromGetter(method.getName());

                Property property = this.getProperties().get(fieldName);
                if (property != null) {
                    Class<?> fieldType = method.getReturnType();

                    Parser parser = this.getTemplate().getParsers().get(fieldType);
                    if (parser == null) {
                        throw new ParserNotFoundException("parser not found for " + fieldType);
                    }

                    try {
                        Object valueObject = method.invoke(object);
                        String[] values = parser.toStringArray(valueObject);
                        property.setValues(values);

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
        for (Map.Entry<String, Property> entry: this.properties.entrySet()) {
            String name = entry.getKey();
            Property property = entry.getValue();
            
            if (this.getTemplate().getRequired().contains(name) && property.isEmpty()) {
                this.errors.put(name, REQUIRED_ERROR);
                continue;
            }

            if (!property.isEmpty()) {
                Class<?> propertyClass = Reflection.getFieldClass(getTemplate().getPropertyClass(), name);
                if (propertyClass == null) {
                    propertyClass = String.class;
                }

                if (!property.isParsed()) {
                    Parser parser = this.getTemplate().getParsers().get(propertyClass);
                    if (parser == null) {
                        this.errors.put(name, PARSER_MISSING_ERROR);
                        continue;
                    }

                    try {
                        property.setParsed(parser.parse(property));

                    } catch (ParseException e) {
                        this.errors.put(name, PARSE_ERROR);
                        continue;
                    }
                }
            }

            Set<Validator> validators = this.getTemplate().getValidators().get(name);
            if (validators != null) {
                for (Validator validator : validators) {
                    if (!validator.isValid(property)) {
                        this.errors.put(name, validator.getKey());
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

                Property property = this.getProperties().get(fieldName);

                if (property != null) {
                    Object parsed = property.getParsed();

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

    public Map<String, Property> getProperties() {
        return properties;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public boolean isValid() {
        return this.errors.isEmpty();
    }

    public Data addProperty(String name) {
        this.properties.put(name, new Property(this));
        return this;
    }

    public Data addProperty(String name, String value) {
        this.properties.put(name, new Property(this, value));
        return this;
    }

    public Data addProperty(String name, String[] values) {
        this.properties.put(name, new Property(this, values));
        return this;
    }

    public Data removeProperty(String name) {
        this.properties.remove(name);
        return this;
    }
}
