package com.linchproject.validator;

/**
 * @author Georg Schmidl
 */
public class Property {

    private Data data;

    private String name;

    private StringValue stringValue;

    private Object parsedValue;

    boolean parsed = false;

    public Property(Data data, String name) {
        this.data = data;
        this.name = name;
    }

    public Property(Data data, String name, StringValue stringValue) {
        this(data, name);
        this.stringValue = stringValue;
    }

    public Data getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public StringValue getStringValue() {
        return stringValue;
    }

    public void setStringValue(StringValue stringValue) {
        this.stringValue = stringValue;
    }

    public Object getParsedValue() {
        return parsedValue;
    }

    public void parse(Parser parser) throws ParseException{
        this.parsedValue = parser.parse(this.stringValue);
        this.parsed = true;
    }

    public boolean isParsed() {
        return parsed;
    }
}
