package com.linchproject.validator.validators;

import com.linchproject.validator.Property;
import com.linchproject.validator.Validator;

import java.util.Arrays;

/**
 * @author Georg Schmidl
 */
public class EqualsOtherValidator implements Validator {

    private String propertyKey;

    public EqualsOtherValidator(String propertyKey) {
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
