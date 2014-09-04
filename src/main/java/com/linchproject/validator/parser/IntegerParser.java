package com.linchproject.validator.parser;

import com.linchproject.validator.*;

/**
 * @author Georg Schmidl
 */
public class IntegerParser implements Parser<Integer> {
    @Override
    public Integer parse(StringValue stringValue) throws ParseException {
        try {
            return stringValue == null ? null : Integer.parseInt(stringValue.get());
        } catch (NumberFormatException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public StringValue toStringValue(Integer object) {
        return object == null ? null : new StringValue(object.toString());
    }
}
