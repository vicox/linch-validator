package com.linchproject.validator;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Georg Schmidl
 */
public class Data extends LinkedHashMap<String, Property> {

    public Data addProperty(String name) {
        super.put(name, new Property(this, name));
        return this;
    }

    public Data addProperty(String name, String value) {
        super.put(name, new Property(this, name, value));
        return this;
    }

    public Data addProperty(String name, String[] values) {
        super.put(name, new Property(this, name, values));
        return this;
    }

    public Data removeProperty(String name) {
        super.remove(name);
        return this;
    }

    @Override
    public Property put(String key, Property value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends Property> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Property remove(Object key) {
        throw new UnsupportedOperationException();
    }

}
