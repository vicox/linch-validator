package com.linchproject.validator.validators;

import com.linchproject.validator.Data;
import com.linchproject.validator.Property;
import junit.framework.TestCase;

public class EqualsOtherPropertyValidatorTest extends TestCase {

    public void testIsValid() throws Exception {
        Data data;
        Property property;

        data = new Data(null).addProperty("c", "d");
        property = new Property(data, "a", "b");
        assertFalse(new EqualsOtherPropertyValidator("c").isValid(property));

        data = new Data(null).addProperty("c", "b");
        property = new Property(data, "a", "b");
        assertTrue(new EqualsOtherPropertyValidator("c").isValid(property));
    }
}