package com.linchproject.validator.parsers;

import com.linchproject.validator.*;

/**
 * @author Georg Schmidl
 */
public class StringParser implements Parser<String> {

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String parse(Value value) throws ParseException {
        return value.getString();
    }

    @Override
    public String[] toStringArray(String string) {
        return new String[] { string };
    }
}
