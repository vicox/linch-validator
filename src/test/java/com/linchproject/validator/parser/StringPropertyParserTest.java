package com.linchproject.validator.parser;

import com.linchproject.validator.Property;
import junit.framework.TestCase;

public class StringPropertyParserTest extends TestCase {

    public void testParse() throws Exception {
        Property property = new Property(null, "test", "a");
        assertEquals("a", new StringPropertyParser().parse(property));
    }

    public void testToStringArray() throws Exception {
        assertEquals("a", new StringPropertyParser().toStringArray("a")[0]);
    }
}