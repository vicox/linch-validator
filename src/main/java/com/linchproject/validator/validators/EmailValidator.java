package com.linchproject.validator.validators;

import com.linchproject.validator.Value;
import com.linchproject.validator.Validator;

/**
 * @author Georg Schmidl
 */
public class EmailValidator implements Validator {

    @Override
    public String getKey() {
        return "email";
    }

    @Override
    public boolean isValid(Value value) {
        for (String string : value.getStrings()) {
            if (!string.contains("@")) {
                return false;
            }
        }
        return true;
    }
}
