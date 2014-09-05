package com.linchproject.validator;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Georg Schmidl
 */
public class Property implements Iterable<String> {

    private Data data;

    private String name;

    private String[] values;

    private Object parsed;

    private boolean isParsed = false;

    public Property(Data data, String name) {
        this.data = data;
        this.name = name;
    }

    public Property(Data data, String name, String[] values) {
        this(data, name);
        this.values = values;
    }

    public Property(Data data, String name, String value) {
        this(data, name);
        this.values = new String[] { value };
    }

    public Data getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return this.values != null &&  this.values.length > 0 ? this.values[0] : null;
    }

    public String[] getValues() {
        return this.values;
    }

    public Object getParsed() {
        return parsed;
    }

    public void parse(PropertyParser propertyParser) throws ParseException{
        this.parsed = propertyParser.parse(this);
        this.isParsed = true;
    }

    public boolean isParsed() {
        return isParsed;
    }

    boolean isEmpty() {
        boolean isEmpty = true;
        if (this.values != null) {
            for (String value: this.values) {
                if (value != null && value.length() > 0) {
                    isEmpty = false;
                    break;
                }
            }
        }
        return isEmpty;
    }

    @Override
    public Iterator<String> iterator() {
        return Arrays.asList(this.values).iterator();
    }

    @Override
    public String toString() {
        return getValue();
    }
}
