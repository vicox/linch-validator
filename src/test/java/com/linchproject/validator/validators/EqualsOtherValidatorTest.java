package com.linchproject.validator.validators;

import com.linchproject.validator.Data;
import com.linchproject.validator.Value;
import junit.framework.TestCase;

public class EqualsOtherValidatorTest extends TestCase {

    public void testIsValid() throws Exception {
        Data data;
        Value value;

        data = new Data().add("c", "d");
        value = new Value(data, "b");
        assertFalse(new EqualsOtherValidator("c").isValid(value));

        data = new Data().add("c", "b");
        value = new Value(data, "b");
        assertTrue(new EqualsOtherValidator("c").isValid(value));
    }
}