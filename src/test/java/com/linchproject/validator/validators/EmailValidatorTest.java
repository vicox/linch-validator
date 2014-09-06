package com.linchproject.validator.validators;

import com.linchproject.validator.Property;
import junit.framework.TestCase;

public class EmailValidatorTest extends TestCase {

    public void testIsValid() throws Exception {
        Property property;

        property = new Property(null, "a");
        assertFalse(new EmailValidator().isValid(property));

        property = new Property(null, "a@example.com");
        assertTrue(new EmailValidator().isValid(property));
    }
}