package com.linchproject.validator;

/**
 * @author Georg Schmidl
 */
public class Error {

    private String key;
    private Object[] arguments;

    public Error(String key) {
        this(key, new Object[]{});
    }

    public Error(String key, Object[] arguments) {
        this.key = key;
        this.arguments = arguments;
    }

    public String getKey() {
        return key;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
