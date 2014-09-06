package com.linchproject.validator;

/**
 * @author Georg Schmidl
 */
public interface Parser<T> {

    T parse(Value value) throws ParseException;

    String[] toStringArray(T object);
}
