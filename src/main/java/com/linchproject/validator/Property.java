package com.linchproject.validator;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Georg Schmidl
 */
public class Property implements Iterable<String> {

    private Data data;

    private String name;

    private String[] stringValues;

    private Object parsedValue;

    private boolean parsed = false;

    public Property(Data data, String name) {
        this.data = data;
        this.name = name;
    }

    public Property(Data data, String name, String[] values) {
        this(data, name);
        this.stringValues = values;
    }

    public Property(Data data, String name, String value) {
        this(data, name);
        this.stringValues = new String[] { value };
    }

    public Data getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getStringValue() {
        return this.stringValues != null &&  this.stringValues.length > 0 ? this.stringValues[0] : null;
    }

    public String[] getStringValues() {
        return this.stringValues;
    }

    public Object getParsedValue() {
        return parsedValue;
    }

    public void parse(PropertyParser propertyParser) throws ParseException{
        this.parsedValue = propertyParser.parse(this);
        this.parsed = true;
    }

    public boolean isParsed() {
        return parsed;
    }

    boolean isEmpty() {
        boolean isEmpty = true;
        if (this.stringValues != null) {
            for (String value: this.stringValues) {
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
        return Arrays.asList(this.stringValues).iterator();
    }

    @Override
    public String toString() {
        return getStringValue();
    }
}
