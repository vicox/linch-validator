package com.linchproject.validator;

/**
 * @author Georg Schmidl
 */
public interface Validator {

    String getKey();

    boolean isValid(Property property);
}
