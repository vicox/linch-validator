package com.linchproject.validator;

import junit.framework.TestCase;

public class ValidatorTest extends TestCase {

    public void testCreate() throws Exception {
        Validator validator = new Validator().addFields(A.class);
        Data data;

        data = validator.emptyData();
        assertEquals(1, data.getValues().size());
        assertNull(data.getValues().get("a").getString());
    }

    public class A {

        private String a;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }
}