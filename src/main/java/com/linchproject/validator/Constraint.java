package com.linchproject.validator;

/**
 * @author Georg Schmidl
 */
public interface Constraint {

    String getKey();

    boolean isValid(Value value);
}
