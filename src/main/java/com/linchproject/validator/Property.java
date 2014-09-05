package com.linchproject.validator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Georg Schmidl
 */
public class Property implements Iterable<String> {

    public static String REQUIRED_ERROR = "required";
    public static String PARSER_MISSING_ERROR = "parser.not.found";
    public static String PARSE_ERROR = "invalid.type";

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

    public String validate(Class<?> clazz) {
        if (this.data.getValidation().getRequired().contains(this.getName()) && this.isEmpty()) {
            return REQUIRED_ERROR;
        }

        Class<?> propertyClass = Reflection.getFieldClass(clazz, this.getName());
        if (propertyClass == null) {
            propertyClass = String.class;
        }

        if (!this.isParsed()) {
            Parser parser = this.data.getValidation().getParsers().get(propertyClass);
            if (parser == null) {
                return PARSER_MISSING_ERROR;

            }

            try {
                this.parsed = parser.parse(this);
                this.isParsed = true;

            } catch (ParseException e) {
                return PARSE_ERROR;
            }
        }

        Set<Validator> validators = this.data.getValidation().getValidators().get(this.getName());
        if (validators != null) {
            for (Validator validator : validators) {
                if (!validator.isValid(this)) {
                    return validator.getKey();
                }
            }
        }
        return null;
    }

    public boolean isParsed() {
        return isParsed;
    }

    public Object getParsed() {
        return parsed;
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
