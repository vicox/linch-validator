package com.linchproject.validator.parsers;

import com.linchproject.validator.*;

/**
 * @author Georg Schmidl
 */
public class IntegerParser implements Parser<Integer> {
    @Override
    public Integer parse(Property property) throws ParseException {
        try {
            return property.getValue() == null ? null : Integer.parseInt(property.getValue());
        } catch (NumberFormatException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public String[] toStringArray(Integer object) {
        return object == null ? null : new String[] { object.toString() };
    }
}