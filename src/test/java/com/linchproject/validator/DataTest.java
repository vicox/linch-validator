package com.linchproject.validator;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataTest extends TestCase {

    public void testReadFromMap() throws Exception {
        DataValidator dataValidator = new DataValidator().addFields("a");
        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        Data data = dataValidator.dataFrom(map);

        assertEquals(1, data.getValues().size());
        assertEquals("b", data.getValues().get("a").getString());
    }

    public void testReadFromObject() throws Exception {
        DataValidator dataValidator = new DataValidator().addFields("a");
        A a = new A();
        a.setA("b");
        Data data = dataValidator.dataFrom(a);

        assertEquals(1, data.getValues().size());
        assertEquals("b", data.getValues().get("a").getString());
    }

    public void testValidateNothing() throws Exception {
        Map<String, String[]> map = new HashMap<String, String[]>();
        DataValidator dataValidator = new DataValidator().addFields(A.class);
        Data data = dataValidator.dataFrom(map).validate();

        assertEquals(0, data.getErrors().size());
    }

    public void testValidateRequired() throws Exception {
        Map<String, String[]> map = new HashMap<String, String[]>();
        DataValidator dataValidator = new DataValidator().addFields(A.class).setRequired("a");
        Data data = dataValidator.dataFrom(map).validate();

        assertEquals(1, data.getErrors().size());
        assertEquals(DataValidator.REQUIRED_ERROR, data.getErrors().get("a"));
    }

    public void testValidateParserNoValue() throws Exception {
        Map<String, String[]> map = new HashMap<String, String[]>();
        DataValidator dataValidator = new DataValidator().addFields(B.class);
        Data data = dataValidator.dataFrom(map).validate();

        assertEquals(0, data.getErrors().size());
    }

    public void testValidateParserMissing() throws Exception {
        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        DataValidator dataValidator = new DataValidator().addFields(B.class);
        Data data = dataValidator.dataFrom(map).validate();

        assertEquals(1, data.getErrors().size());
        assertEquals(DataValidator.PARSER_MISSING_ERROR, data.getErrors().get("a"));
    }

    public void testValidateParseError() throws Exception {
        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        DataValidator dataValidator = new DataValidator().addFields(C.class);
        Data data = dataValidator.dataFrom(map).validate();

        assertEquals(1, data.getErrors().size());
        assertEquals(DataValidator.PARSE_ERROR, data.getErrors().get("a"));
    }

    public void testValidateParser() throws Exception {
        Thing thing = new Thing();

        Parser<Thing> parser = mock(Parser.class);
        when(parser.getType()).thenReturn(Thing.class);
        when(parser.parse(any(Value.class))).thenReturn(thing);

        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("a", new String[]{"thing"});
        DataValidator dataValidator = new DataValidator().addParser(parser).addFields(B.class);
        Data data = dataValidator.dataFrom(map).validate();

        assertEquals(0, data.getErrors().size());
        assertEquals(thing, data.get("a"));
    }

    public void testValidateParserForKey() throws Exception {
        Thing thing = new Thing();

        Parser<Thing> parser = mock(Parser.class);
        when(parser.getType()).thenReturn(Thing.class);

        Parser<Thing> parserForKey = mock(Parser.class);
        when(parserForKey.getType()).thenReturn(Thing.class);
        when(parserForKey.parse(any(Value.class))).thenReturn(thing);

        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("a", new String[]{"thing"});
        DataValidator dataValidator = new DataValidator()
                .addParser(parser)
                .addParser("a", parserForKey)
                .addFields(B.class);
        Data data = dataValidator.dataFrom(map).validate();

        assertEquals(0, data.getErrors().size());
        assertEquals(thing, data.get("a"));
    }

    public void testWriteTo() throws Exception {
        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("a", new String[]{"b"});
        DataValidator dataValidator = new DataValidator().addFields(A.class);
        Data data = dataValidator.dataFrom(map).validate();

        A a = new A();
        data.writeTo(a);

        assertEquals("b", a.getA());
    }

    public void testWriteToSelected() throws Exception {
        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("a", new String[]{"a"});
        map.put("b", new String[]{"b"});
        DataValidator dataValidator = new DataValidator().addFields(AB.class);
        Data data = dataValidator.dataFrom(map).validate();

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