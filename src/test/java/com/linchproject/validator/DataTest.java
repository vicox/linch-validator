package com.linchproject.validator;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class DataTest extends TestCase {

    public void testReadFrom() throws Exception {
        DataValidator dataValidator = new DataValidator().addFields("a");
        Data data;

        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        data = dataValidator.createDataFrom(map);
        assertEquals(1, data.getValues().size());
        assertEquals("b", data.getValues().get("a").getString());

        A a = new A();
        a.setA("b");
        data = dataValidator.createDataFrom(a);
        assertEquals(1, data.getValues().size());
        assertEquals("b", data.getValues().get("a").getString());
    }

    public void testValidate() throws Exception {
        Map<String, String[]> map;
        DataValidator dataValidator;
        Data data;

        map = new HashMap<String, String[]>();
        dataValidator = new DataValidator().addFields(A.class);
        data = dataValidator.createDataFrom(map).validate();
        assertEquals(0, data.getErrors().size());

        map = new HashMap<String, String[]>();
        dataValidator = new DataValidator().addFields(A.class).setRequired("a");
        data = dataValidator.createDataFrom(map).validate();
        assertEquals(1, data.getErrors().size());
        assertEquals("required", data.getErrors().get("a"));

        map = new HashMap<String, String[]>();
        dataValidator = new DataValidator().addFields(A.class).setRequired("a");
        data = dataValidator.createDataFrom(map).validate();
        assertEquals(1, data.getErrors().size());
        assertEquals(DataValidator.REQUIRED_ERROR, data.getErrors().get("a"));

        map = new HashMap<String, String[]>();
        dataValidator = new DataValidator().addFields(B.class);
        data = dataValidator.createDataFrom(map).validate();
        assertEquals(0, data.getErrors().size());

        map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        dataValidator = new DataValidator().addFields(B.class);
        data = dataValidator.createDataFrom(map).validate();
        assertEquals(1, data.getErrors().size());
        assertEquals(DataValidator.PARSER_MISSING_ERROR, data.getErrors().get("a"));

        map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        dataValidator = new DataValidator().addFields(C.class);
        data = dataValidator.createDataFrom(map).validate();
        assertEquals(1, data.getErrors().size());
        assertEquals(DataValidator.PARSE_ERROR, data.getErrors().get("a"));
    }

    public void testWriteTo() throws Exception {
        Map<String, String[]> map;
        DataValidator dataValidator;
        Data data;

        map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        dataValidator = new DataValidator().addFields(A.class);
        data = dataValidator.createDataFrom(map).validate();

        A a = new A();
        assertNull(a.getA());

        data.writeTo(a);
        assertEquals("b", a.getA());

        map = new HashMap<String, String[]>();
        map.put("a", new String[]{"a"});
        map.put("b", new String[]{"b"});
        dataValidator = new DataValidator().addFields(AB.class);
        data = dataValidator.createDataFrom(map).validate();
        AB ab = new AB();
        data.writeTo(ab, "a");
        assertEquals("a", ab.getA());
        assertNull(ab.getB());
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

    public class AB {

        private String a;
        private String b;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }
    }
}