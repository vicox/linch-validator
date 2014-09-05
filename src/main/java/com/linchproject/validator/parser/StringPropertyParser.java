package com.linchproject.validator.parser;

import com.linchproject.validator.*;

/**
 * @author Georg Schmidl
 */
public class StringPropertyParser implements PropertyParser<String> {

    @Override
    public String parse(Property property) throws ParseException {
        return property.getStringValue();
    }

    @Override
    public String[] toStringArray(String string) {
        return new String[] { string };
    }
}
