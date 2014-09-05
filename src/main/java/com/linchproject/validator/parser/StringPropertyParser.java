package com.linchproject.validator.parser;

import com.linchproject.validator.*;

/**
 * @author Georg Schmidl
 */
public class StringPropertyParser implements PropertyParser<String> {

    @Override
    public String parse(Property property) throws ParseException {
        return property.getStringValue().get();
    }

    @Override
    public StringValue toStringValue(String object) {
        return new StringValue(object);
    }
}
