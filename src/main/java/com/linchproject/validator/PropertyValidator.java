package com.linchproject.validator;

/**
 * @author Georg Schmidl
 */
public interface PropertyValidator {

    String getKey();

    boolean isValid(Property property);
}
