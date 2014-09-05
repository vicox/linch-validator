package com.linchproject.validator;

/**
 * @author Georg Schmidl
 */
public interface PropertyParser<T> {

    T parse(Property property) throws ParseException;

    StringValue toStringValue(T object);
}
