package com.linchproject.validator;

/**
 * @author Georg Schmidl
 */
public interface Parser<T> {

    T parse(StringValue stringValue) throws ParseException;

    StringValue toStringValue(T object);
}
