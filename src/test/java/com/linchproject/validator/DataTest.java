package com.linchproject.validator;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class DataTest extends TestCase {

    public void testValidate() throws Exception {
        Map<String, String[]> map;
        Validator validator;
        Data data;

        map = new HashMap<String, String[]>();
        validator = new Validator();
        data = validator.create(map).validate(A.class);
        assertEquals(0, data.getErrors().size());

        map = new HashMap<String, String[]>();
        validator = new Validator().setRequired("a");
        data = validator.create(map).validate(A.class);
        assertEquals(1, data.getErrors().size());
        assertEquals("required", data.getErrors().get("a"));

        map = new HashMap<String, String[]>();
        validator = new Validator().setRequired("a");
        data = validator.create(map).validate(A.class);
        assertEquals(1, data.getErrors().size());
        assertEquals(Property.REQUIRED_ERROR, data.getErrors().get("a"));

        map = new HashMap<String, String[]>();
        validator = new Validator();
        data = validator.create(map).validate(B.class);
        assertEquals(0, data.getErrors().size());

        map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        validator = new Validator();
        data = validator.create(map).validate(B.class);
        assertEquals(1, data.getErrors().size());
        assertEquals(Property.PARSER_MISSING_ERROR, data.getErrors().get("a"));

        map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        validator = new Validator();
        data = validator.create(map).validate(C.class);
        assertEquals(1, data.getErrors().size());
        assertEquals(Property.PARSE_ERROR, data.getErrors().get("a"));
    }

    public void testDump() throws Exception {
        Map<String, String[]> map;
        Validator validator;
        Data data;

        map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        validator = new Validator();
        data = validator.create(map).validate(A.class);

        A a = new A();
        assertNull(a.getA());

        data.dump(a);
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