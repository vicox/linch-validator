package com.linchproject.validator.parsers;

import com.linchproject.validator.Property;
import junit.framework.TestCase;

public class StringParserTest extends TestCase {

    public void testParse() throws Exception {
        Property property = new Property(null, "a");
        assertEquals("a", new StringParser().parse(property));
    }

    public void testToStringArray() throws Exception {
        assertEquals("a", new StringParser().toStringArray("a")[0]);
    }
}