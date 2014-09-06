package com.linchproject.validator;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Georg Schmidl
 */
public class Value<T> implements Iterable<String> {

    private Data data;

    private String[] strings;

    private T parsed;

    private boolean isParsed = false;

    public Value(Data data) {
        this.data = data;
    }

    public Value(Data data, String[] strings) {
        this(data);
        this.strings = strings;
    }

    public Value(Data data, String string) {
        this(data);
        this.strings = new String[] { string };
    }

    public Data getData() {
        return data;
    }

    public String getString() {
        return this.strings != null &&  this.strings.length > 0 ? this.strings[0] : null;
    }

    public String[] getStrings() {
        return this.strings;
    }

    public void setStrings(String[] strings) {
        this.strings = strings;
        this.parsed = null;
        this.isParsed = false;
    }

    public boolean isParsed() {
        return isParsed;
    }

    public T getParsed() {
        return parsed;
    }

    public void setParsed(T parsed) {
        this.parsed = parsed;
        this.isParsed = true;
    }

    boolean isEmpty() {
        boolean isEmpty = true;
        if (this.strings != null) {
            for (String value: this.strings) {
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
        return Arrays.asList(this.strings).iterator();
    }

    @Override
    public String toString() {
        return getString();
    }
}
