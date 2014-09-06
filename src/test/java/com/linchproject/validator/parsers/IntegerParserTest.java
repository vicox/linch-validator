package com.linchproject.validator.parsers;

import com.linchproject.validator.Property;
import junit.framework.TestCase;

public class IntegerParserTest extends TestCase {

    public void testParse() throws Exception {
        Property property = new Property(null, "1");
        assertEquals(Integer.valueOf(1), new IntegerParser().parse(property));
    }

    public void testToStringArray() throws Exception {
        assertEquals("1", new IntegerParser().toStringArray(1)[0]);
    }
}