package com.linchproject.validator.parser;

import com.linchproject.validator.*;

/**
 * @author Georg Schmidl
 */
public class IntegerPropertyParser implements PropertyParser<Integer> {
    @Override
    public Integer parse(Property property) throws ParseException {
        StringValue stringValue = property.getStringValue();
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
