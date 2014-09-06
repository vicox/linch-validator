package com.linchproject.validator;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class DataTest extends TestCase {

    public void testReadFrom() throws Exception {
        Validation validation = new Validation().setProperties(new String[]{"a"});
        Data data;

        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        data = validation.create(map);
        assertEquals(1, data.getProperties().size());
        assertEquals("b", data.getProperties().get("a").getValue());

        A a = new A();
        a.setA("b");
        data = validation.create(a);
        assertEquals(1, data.getProperties().size());
        assertEquals("b", data.getProperties().get("a").getValue());
    }

    public void testValidate() throws Exception {
        Map<String, String[]> map;
        Validation validation;
        Data data;

        map = new HashMap<String, String[]>();
        validation = new Validation().setClazz(A.class);
        data = validation.create(map).validate();
        assertEquals(0, data.getErrors().size());

        map = new HashMap<String, String[]>();
        validation = new Validation().setClazz(A.class).setRequired("a");
        data = validation.create(map).validate();
        assertEquals(1, data.getErrors().size());
        assertEquals("required", data.getErrors().get("a"));

        map = new HashMap<String, String[]>();
        validation = new Validation().setClazz(A.class).setRequired("a");
        data = validation.create(map).validate();
        assertEquals(1, data.getErrors().size());
        assertEquals(Property.REQUIRED_ERROR, data.getErrors().get("a"));

        map = new HashMap<String, String[]>();
        validation = new Validation().setClazz(B.class);
        data = validation.create(map).validate();
        assertEquals(0, data.getErrors().size());

        map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        validation = new Validation().setClazz(B.class);
        data = validation.create(map).validate();
        assertEquals(1, data.getErrors().size());
        assertEquals(Property.PARSER_MISSING_ERROR, data.getErrors().get("a"));

        map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        validation = new Validation().setClazz(C.class);
        data = validation.create(map).validate();
        assertEquals(1, data.getErrors().size());
        assertEquals(Property.PARSE_ERROR, data.getErrors().get("a"));
    }

    public void testWriteTo() throws Exception {
        Map<String, String[]> map;
        Validation validation;
        Data data;

        map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        validation = new Validation().setClazz(A.class);
        data = validation.create(map).validate();

        A a = new A();
        assertNull(a.getA());

        data.writeTo(a);
        assertEquals("b", a.getA());
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

    public class Thing {

    }

    public class B {

        private Thing a;

        public Thing getA() {
            return a;
        }

        public void setA(Thing a) {
            this.a = a;
        }
    }

    public class C {

        private Integer a;

        public Integer getA() {
            return a;
        }

        public void setA(Integer a) {
            this.a = a;
        }
    }
}