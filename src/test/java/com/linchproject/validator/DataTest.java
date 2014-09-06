package com.linchproject.validator;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class DataTest extends TestCase {

    public void testReadFrom() throws Exception {
        Template template = new Template().setProperties(new String[]{"a"});
        Data data;

        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        data = template.create(map);
        assertEquals(1, data.getProperties().size());
        assertEquals("b", data.getProperties().get("a").getValue());

        A a = new A();
        a.setA("b");
        data = template.create(a);
        assertEquals(1, data.getProperties().size());
        assertEquals("b", data.getProperties().get("a").getValue());
    }

    public void testValidate() throws Exception {
        Map<String, String[]> map;
        Template template;
        Data data;

        map = new HashMap<String, String[]>();
        template = new Template().setPropertyClass(A.class);
        data = template.create(map).validate();
        assertEquals(0, data.getErrors().size());

        map = new HashMap<String, String[]>();
        template = new Template().setPropertyClass(A.class).setRequired("a");
        data = template.create(map).validate();
        assertEquals(1, data.getErrors().size());
        assertEquals("required", data.getErrors().get("a"));

        map = new HashMap<String, String[]>();
        template = new Template().setPropertyClass(A.class).setRequired("a");
        data = template.create(map).validate();
        assertEquals(1, data.getErrors().size());
        assertEquals(Data.REQUIRED_ERROR, data.getErrors().get("a"));

        map = new HashMap<String, String[]>();
        template = new Template().setPropertyClass(B.class);
        data = template.create(map).validate();
        assertEquals(0, data.getErrors().size());

        map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        template = new Template().setPropertyClass(B.class);
        data = template.create(map).validate();
        assertEquals(1, data.getErrors().size());
        assertEquals(Data.PARSER_MISSING_ERROR, data.getErrors().get("a"));

        map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        template = new Template().setPropertyClass(C.class);
        data = template.create(map).validate();
        assertEquals(1, data.getErrors().size());
        assertEquals(Data.PARSE_ERROR, data.getErrors().get("a"));
    }

    public void testWriteTo() throws Exception {
        Map<String, String[]> map;
        Template template;
        Data data;

        map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        template = new Template().setPropertyClass(A.class);
        data = template.create(map).validate();

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