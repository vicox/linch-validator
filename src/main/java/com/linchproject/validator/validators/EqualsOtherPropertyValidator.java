package com.linchproject.validator.validators;

import com.linchproject.validator.Property;
import com.linchproject.validator.PropertyValidator;

import java.util.Arrays;

/**
 * @author Georg Schmidl
 */
public class EqualsOtherPropertyValidator implements PropertyValidator {

    private String propertyKey;

    public EqualsOtherPropertyValidator(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    @Override
    public String getKey() {
        return "equals";
    }

    @Override
    public boolean isValid(Property property) {
        Property otherProperty = property.getData().getProperties().get(propertyKey);

        String[] otherValues = otherProperty != null ? otherProperty.getValues() : null;
        return Arrays.equals(property.getValues(), otherValues);
    }
}
