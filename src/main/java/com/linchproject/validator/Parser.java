package com.linchproject.validator;

/**
 * @author Georg Schmidl
 */
public interface Parser<T> {

    Class<T> getType();

    T parse(Value value) throws ParseException;

    String[] toStringArray(T object);
}
