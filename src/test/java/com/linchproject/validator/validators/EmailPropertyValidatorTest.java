package com.linchproject.validator.validators;

import com.linchproject.validator.Property;
import junit.framework.TestCase;

public class EmailPropertyValidatorTest extends TestCase {

    public void testIsValid() throws Exception {
        Property property;

        property = new Property(null, "test", "a");
        assertFalse(new EmailPropertyValidator().isValid(property));

        property = new Property(null, "test", "a@example.com");
        assertTrue(new EmailPropertyValidator().isValid(property));
    }
}