package com.linchproject.validator;

import junit.framework.TestCase;

public class DataValidatorTest extends TestCase {

    public void testCreate() throws Exception {
        DataValidator dataValidator = new DataValidator().addFields(A.class);
        Data data;

        data = dataValidator.createEmptyData();
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