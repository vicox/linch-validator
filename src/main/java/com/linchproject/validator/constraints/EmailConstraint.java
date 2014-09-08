package com.linchproject.validator.constraints;

import com.linchproject.validator.Value;
import com.linchproject.validator.Constraint;

/**
 * @author Georg Schmidl
 */
public class EmailConstraint implements Constraint {

    @Override
    public Result check(Value value) {
        for (String string : value.getStrings()) {
            if (!string.contains("@")) {
                return Result.error("email.invalid");
            }
        }
        return Result.ok();
    }
}
