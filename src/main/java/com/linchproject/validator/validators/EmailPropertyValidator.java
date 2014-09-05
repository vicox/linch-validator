package com.linchproject.validator.validators;

import com.linchproject.validator.Property;
import com.linchproject.validator.PropertyValidator;

/**
 * @author Georg Schmidl
 */
public class EmailPropertyValidator implements PropertyValidator {

    @Override
    public String getKey() {
        return "email";
    }

    @Override
    public boolean isValid(Property property) {
        for (String value : property.getValues()) {
            if (!value.contains("@")) {
                return false;
            }
        }
        return true;
    }
}
