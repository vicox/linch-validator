package com.linchproject.validator;

/**
 * @author Georg Schmidl
 */
public class ParserNotFoundException extends RuntimeException {

    public ParserNotFoundException() {
        super();
    }

    public ParserNotFoundException(String message) {
        super(message);
    }

    public ParserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserNotFoundException(Throwable cause) {
        super(cause);
    }
}
