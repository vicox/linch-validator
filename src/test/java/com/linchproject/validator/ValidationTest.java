package com.linchproject.validator;

import junit.framework.TestCase;

public class ValidationTest extends TestCase {

    public void testCreate() throws Exception {
        Validation validation = new Validation().setClazz(A.class);
        Data data;

        data = validation.create();
        assertEquals(1, data.getProperties().size());
        assertNull(data.getProperties().get("a").getValue());
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