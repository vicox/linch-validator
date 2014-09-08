package com.linchproject.validator;

/**
 * @author Georg Schmidl
 */
public class ErrorMessage {
    private String key;
    private Object[] arguments;

    public ErrorMessage(String key) {
        this(key, new Object[]{});
    }

    public ErrorMessage(String key, Object[] arguments) {
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
