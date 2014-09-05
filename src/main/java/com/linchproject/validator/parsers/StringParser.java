package com.linchproject.validator.parsers;

import com.linchproject.validator.*;

/**
 * @author Georg Schmidl
 */
public class StringParser implements Parser<String> {

    @Override
    public String parse(Property property) throws ParseException {
        return property.getValue();
    }

    @Override
    public String[] toStringArray(String string) {
        return new String[] { string };
    }
}
