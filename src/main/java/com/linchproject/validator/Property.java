package com.linchproject.validator;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Georg Schmidl
 */
public class Property implements Iterable<String> {

    private Data data;

    private String[] values;

    private Object parsed;

    private boolean isParsed = false;

    public Property(Data data) {
        this.data = data;
    }

    public Property(Data data, String[] values) {
        this(data);
        this.values = values;
    }

    public Property(Data data, String value) {
        this(data);
        this.values = new String[] { value };
    }

    public Data getData() {
        return data;
    }

    public String getValue() {
        return this.values != null &&  this.values.length > 0 ? this.values[0] : null;
    }

    public String[] getValues() {
        return this.values;
    }

    public void setValues(String[] values) {
        this.values = values;
        this.parsed = null;
        this.isParsed = false;
    }

    public boolean isParsed() {
        return isParsed;
    }

    public Object getParsed() {
        return parsed;
    }

    public void setParsed(Object parsed) {
        this.parsed = parsed;
        this.isParsed = true;
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
