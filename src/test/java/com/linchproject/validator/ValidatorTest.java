package com.linchproject.validator;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class ValidatorTest extends TestCase {

    public void testRead() throws Exception {
        Validator validator = new Validator();
        Data data;

        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        data = validator.read(map);
        assertEquals(1, data.getProperties().size());
        assertEquals("b", data.getProperties().get("a").getValue());

        A a = new A();
        a.setA("b");
        data = validator.read(a);
        assertEquals(1, data.getProperties().size());
        assertEquals("b", data.getProperties().get("a").getValue());
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