package com.linchproject.validator.parsers;

import com.linchproject.validator.*;

/**
 * @author Georg Schmidl
 */
public class IntegerParser implements Parser<Integer> {
    @Override
    public Integer parse(Value value) throws ParseException {
        try {
            return value.getString() == null ? null : Integer.parseInt(value.getString());
        } catch (NumberFormatException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public String[] toStringArray(Integer object) {
        return object == null ? null : new String[] { object.toString() };
    }
}
