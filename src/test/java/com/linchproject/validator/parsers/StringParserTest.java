package com.linchproject.validator.parsers;

import com.linchproject.validator.Value;
import junit.framework.TestCase;

public class StringParserTest extends TestCase {

    public void testParse() throws Exception {
        Value value = new Value(null, "a");
        assertEquals("a", new StringParser().parse(value));
    }

    public void testToStringArray() throws Exception {
        assertEquals("a", new StringParser().toStringArray("a")[0]);
    }
}