package com.linchproject.validator.parser;

import com.linchproject.validator.Property;
import junit.framework.TestCase;

public class IntegerPropertyParserTest extends TestCase {

    public void testParse() throws Exception {
        Property property = new Property(null, "test", "1");
        assertEquals(Integer.valueOf(1), new IntegerPropertyParser().parse(property));
    }

    public void testToStringArray() throws Exception {
        assertEquals("1", new IntegerPropertyParser().toStringArray(1)[0]);
    }
}