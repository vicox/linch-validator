package com.linchproject.validator;

import junit.framework.TestCase;

public class TemplateTest extends TestCase {

    public void testCreate() throws Exception {
        Template template = new Template().setClazz(A.class);
        Data data;

        data = template.create();
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