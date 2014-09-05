package com.linchproject.validator.validators;

import com.linchproject.validator.Property;
import junit.framework.TestCase;

public class EmailValidatorTest extends TestCase {

    public void testIsValid() throws Exception {
        Property property;

        property = new Property(null, "test", "a");
        assertFalse(new EmailValidator().isValid(property));

        property = new Property(null, "test", "a@example.com");
        assertTrue(new EmailValidator().isValid(property));
    }
}