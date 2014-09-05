package com.linchproject.validator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Georg Schmidl
 */
public class Data {

    private Validation validation;

    private Map<String, Property> properties = new LinkedHashMap<String, Property>();

    private Map<String, String> errors = new LinkedHashMap<String, String>();

    private boolean validated;

    public Data(Validation validation) {
        this.validation = validation;
    }

    public Data validate(Class<?> clazz) {
        for (Property property: this.properties.values()) {
            String error = property.validate(clazz);
            if (error != null) {
                this.errors.put(property.getName(), error);
            }
        }

        for (String requiredPropertyName : this.validation.getRequired()) {
            Property property = this.properties.get(requiredPropertyName);
            if (property == null) {
                property = new Property(this, requiredPropertyName);
                this.properties.put(requiredPropertyName, property);
                String error = property.validate(clazz);
                if (error != null) {
                    this.errors.put(property.getName(), error);
                }
            }
        }

        this.validated = true;
        return this;
    }

    public void dump(Object object) {
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

    public Validation getValidation() {
        return validation;
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
