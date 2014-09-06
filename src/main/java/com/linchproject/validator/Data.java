package com.linchproject.validator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Georg Schmidl
 */
public class Data {

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
        for (Property property: this.properties.values()) {
            String error = property.validate(getTemplate().getClazz());
            if (error != null) {
                this.errors.put(property.getName(), error);
            }
        }

        for (String requiredPropertyName : this.template.getRequired()) {
            Property property = this.properties.get(requiredPropertyName);
            if (property == null) {
                property = new Property(this, requiredPropertyName);
                this.properties.put(requiredPropertyName, property);
                String error = property.validate(getTemplate().getClazz());
                if (error != null) {
                    this.errors.put(property.getName(), error);
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
        this.properties.put(name, new Property(this, name));
        return this;
    }

    public Data addProperty(String name, String value) {
        this.properties.put(name, new Property(this, name, value));
        return this;
    }

    public Data addProperty(String name, String[] values) {
        this.properties.put(name, new Property(this, name, values));
        return this;
    }

    public Data removeProperty(String name) {
        this.properties.remove(name);
        return this;
    }
}
