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
    public Result check(Value value) {
        Value otherValue = value.getData().getValues().get(this.otherKey);

        String[] strings = value.getStrings();
        String[] otherStrings = otherValue != null ? otherValue.getStrings() : null;
        return Arrays.equals(strings, otherStrings) ? Result.ok() : Result.error("not.equals.other", this.otherKey);
    }
}
