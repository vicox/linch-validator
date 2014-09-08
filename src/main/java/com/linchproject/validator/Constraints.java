package com.linchproject.validator;

import com.linchproject.validator.constraints.EmailConstraint;

/**
 * @author Georg Schmidl
 */
public class Constraints {

    public static Constraint EMAIL = new EmailConstraint();
}
