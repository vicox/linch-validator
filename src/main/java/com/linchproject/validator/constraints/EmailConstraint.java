package com.linchproject.validator.constraints;

import com.linchproject.validator.Value;
import com.linchproject.validator.Constraint;

/**
 * @author Georg Schmidl
 */
public class EmailConstraint implements Constraint {

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
