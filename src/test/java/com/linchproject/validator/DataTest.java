package com.linchproject.validator;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class DataTest extends TestCase {

    public void testReadFrom() throws Exception {
        ValidationTemplate validationTemplate = new ValidationTemplate().setKeys(new String[]{"a"});
        Data data;

        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        data = validationTemplate.createDataFrom(map);
        assertEquals(1, data.getValues().size());
        assertEquals("b", data.getValues().get("a").getString());

        A a = new A();
        a.setA("b");
        data = validationTemplate.createDataFrom(a);
        assertEquals(1, data.getValues().size());
        assertEquals("b", data.getValues().get("a").getString());
    }

    public void testValidate() throws Exception {
        Map<String, String[]> map;
        ValidationTemplate validationTemplate;
        Data data;

        map = new HashMap<String, String[]>();
        validationTemplate = new ValidationTemplate().setClazz(A.class);
        data = validationTemplate.createDataFrom(map).validate();
        assertEquals(0, data.getErrors().size());

        map = new HashMap<String, String[]>();
        validationTemplate = new ValidationTemplate().setClazz(A.class).setRequired("a");
        data = validationTemplate.createDataFrom(map).validate();
        assertEquals(1, data.getErrors().size());
        assertEquals("required", data.getErrors().get("a"));

        map = new HashMap<String, String[]>();
        validationTemplate = new ValidationTemplate().setClazz(A.class).setRequired("a");
        data = validationTemplate.createDataFrom(map).validate();
        assertEquals(1, data.getErrors().size());
        assertEquals(Data.REQUIRED_ERROR, data.getErrors().get("a"));

        map = new HashMap<String, String[]>();
        validationTemplate = new ValidationTemplate().setClazz(B.class);
        data = validationTemplate.createDataFrom(map).validate();
        assertEquals(0, data.getErrors().size());

        map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        validationTemplate = new ValidationTemplate().setClazz(B.class);
        data = validationTemplate.createDataFrom(map).validate();
        assertEquals(1, data.getErrors().size());
        assertEquals(Data.PARSER_MISSING_ERROR, data.getErrors().get("a"));

        map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        validationTemplate = new ValidationTemplate().setClazz(C.class);
        data = validationTemplate.createDataFrom(map).validate();
        assertEquals(1, data.getErrors().size());
        assertEquals(Data.PARSE_ERROR, data.getErrors().get("a"));
    }

    public void testWriteTo() throws Exception {
        Map<String, String[]> map;
        ValidationTemplate validationTemplate;
        Data data;

        map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        validationTemplate = new ValidationTemplate().setClazz(A.class);
        data = validationTemplate.createDataFrom(map).validate();

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