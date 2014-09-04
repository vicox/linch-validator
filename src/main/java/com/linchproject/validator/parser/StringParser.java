package com.linchproject.validator.parser;

import com.linchproject.validator.*;

/**
 * @author Georg Schmidl
 */
public class StringParser implements Parser<String> {

    @Override
    public String parse(StringValue stringValue) throws ParseException {
        return stringValue.get();
    }

    @Override
    public StringValue toStringValue(String object) {
        return new StringValue(object);
    }
}
