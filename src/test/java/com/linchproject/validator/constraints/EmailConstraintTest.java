package com.linchproject.validator.constraints;

import com.linchproject.validator.Value;
import junit.framework.TestCase;

public class EmailConstraintTest extends TestCase {

    public void testIsValid() throws Exception {
        Value value;

        value = new Value(null, "a");
        assertFalse(new EmailConstraint().check(value).isOk());

        value = new Value(null, "a@example.com");
        assertTrue(new EmailConstraint().check(value).isOk());
    }
}