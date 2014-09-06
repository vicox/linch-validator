package com.linchproject.validator.validators;

import com.linchproject.validator.Value;
import com.linchproject.validator.Validator;

import java.util.Arrays;

/**
 * @author Georg Schmidl
 */
public class EqualsOtherValidator implements Validator {

    private String otherKey;

    public EqualsOtherValidator(String otherKey) {
        this.otherKey = otherKey;
    }

    @Override
    public String getKey() {
        return "equals";
    }

    @Override
    public boolean isValid(Value value) {
        Value otherValue = value.getData().getValues().get(this.otherKey);

        String[] otherStrings = otherValue != null ? otherValue.getStrings() : null;
        return Arrays.equals(value.getStrings(), otherStrings);
    }
}
