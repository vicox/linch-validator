package com.linchproject.validator;

/**
 * @author Georg Schmidl
 */
public interface PropertyParser<T> {

    T parse(StringValue stringValue) throws ParseException;

    StringValue toStringValue(T object);
}
