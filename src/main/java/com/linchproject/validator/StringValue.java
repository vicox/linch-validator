package com.linchproject.validator;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Georg Schmidl
 */
public class StringValue implements Iterable<String> {

    private String[] values;

    public StringValue(String[] values) {
        this.values = values;
    }

    public StringValue(String value) {
        this.values = new String[] {value};
    }

    @Override
    public Iterator<String> iterator() {
        return Arrays.asList(this.values).iterator();
    }

    @Override
    public String toString() {
        return get();
    }

    public String get() {
        return this.values != null &&  this.values.length > 0 ? this.values[0] : null;
    }

    public String[] getAll() {
        return this.values;
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
}
