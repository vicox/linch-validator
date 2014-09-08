package com.linchproject.validator.constraints;

import com.linchproject.validator.Value;
import com.linchproject.validator.Constraint;

import java.util.Arrays;

/**
 * @author Georg Schmidl
 */
public class EqualsOtherConstraint implements Constraint {

    private String otherKey;

    public EqualsOtherConstraint(String otherKey) {
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
