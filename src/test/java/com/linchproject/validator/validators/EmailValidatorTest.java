package com.linchproject.validator.validators;

import com.linchproject.validator.Value;
import junit.framework.TestCase;

public class EmailValidatorTest extends TestCase {

    public void testIsValid() throws Exception {
        Value value;

        value = new Value(null, "a");
        assertFalse(new EmailValidator().isValid(value));

        value = new Value(null, "a@example.com");
        assertTrue(new EmailValidator().isValid(value));
    }
}