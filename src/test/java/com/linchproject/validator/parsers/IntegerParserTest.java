package com.linchproject.validator.parsers;

import com.linchproject.validator.Value;
import junit.framework.TestCase;

public class IntegerParserTest extends TestCase {

    public void testParse() throws Exception {
        Value value = new Value(null, "1");
        assertEquals(Integer.valueOf(1), new IntegerParser().parse(value));
    }

    public void testToStringArray() throws Exception {
        assertEquals("1", new IntegerParser().toStringArray(1)[0]);
    }
}