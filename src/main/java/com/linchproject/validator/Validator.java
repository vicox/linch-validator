package com.linchproject.validator;

import com.linchproject.validator.parsers.IntegerParser;
import com.linchproject.validator.parsers.StringParser;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Georg Schmidl
 */
public class Validator {

    public static ErrorMessage REQUIRED_ERROR_MESSAGE = new ErrorMessage("required");
    public static ErrorMessage PARSER_MISSING_ERROR_MESSAGE = new ErrorMessage("parser.not.found");
    public static ErrorMessage PARSE_ERROR_MESSAGE = new ErrorMessage("invalid.type");

    private Map<String, Class<?>> fields = new LinkedHashMap<String, Class<?>>();

    private Set<String> required = new HashSet<String>();

    private Map<Class<?>, Parser<?>> parsers = new HashMap<Class<?>, Parser<?>>();

    private Map<String, Parser<?>> fieldParsers = new HashMap<String, Parser<?>>();

    public Validator() {
        addParser(new StringParser());
        addParser(new IntegerParser());
    }

    private Map<String, Set<Constraint>> constraints = new HashMap<String, Set<Constraint>>();

    public Data emptyData() {
        return new Data(this);
    }

    public Data dataFrom(Object object) {
        return emptyData().readFrom(object);
    }

    public Data dataFrom(Map<String, String[]> map) {
        return emptyData().readFrom(map);
    }

    public Validator addField(String key) {
        return this.addFields(key);
    }

    public Validator addField(String key, Class<?> type) {
        this.fields.put(key, type);
        return this;
    }

    public Validator addFields(String... keys) {
        return addFields(Arrays.asList(keys));
    }

    public Validator addFields(Collection<String> keys, String... moreKeys) {
        for (String key: keys) {
            this.fields.put(key, String.class);
        }
        return moreKeys.length > 0 ? addFields(moreKeys) : this;
    }

    public Validator addFields(Map<String, Class<?>> fields, String... keys) {
        this.fields.putAll(fields);
        return keys.length > 0 ? addFields(keys) : this;
    }

    public Validator addFields(Class<?> clazz, String... keys) {
        for (Method method: clazz.getDeclaredMethods()) {
            if (Reflection.isGetter(method)) {
                String key = Reflection.getNameFromGetter(method.getName());
                Class<?> type = method.getReturnType();
                this.fields.put(key, type);
            }
        }
        return keys.length > 0 ? addFields(keys) : this;
    }

    public Validator removeField(String key) {
        return removeFields(key);
    }

    public Validator removeFields(String... keys) {
        for (String key: keys) {
            this.fields.remove(key);
        }
        return this;
    }

    public Class<?> getFieldType(String key) {
        return this.fields.get(key);
    }

    public Validator setRequired(String key) {
        this.required.add(key);
        return this;
    }

    public Validator setRequired(String... keys) {
        Collections.addAll(this.required, keys);
        return this;
    }

    public Validator setAllRequired() {
        this.required.addAll(fields.keySet());
        return this;
    }

    public Validator addParser(Parser<?> parser) {
        this.parsers.put(parser.getType(), parser);
        return this;
    }

    public Validator addParser(String key, Parser<?> parser) {
        this.fieldParsers.put(key, parser);
        return this;
    }

    public Validator addConstraint(String key, Constraint constraint) {
        return this.addConstraint(key, constraint);
    }

    public Validator addConstraint(String key, Constraint... constraints) {
        if (this.constraints.get(key) == null) {
            this.constraints.put(key, new HashSet<Constraint>());
        }
        Collections.addAll(this.constraints.get(key), constraints);
        return this;
    }

    public void validate(Data data) {
        data.getErrors().clear();

        for (Map.Entry<String, Value<?>> entry: data.getValues().entrySet()) {
            String key = entry.getKey();
            Value value = entry.getValue();

            if (this.isRequired(key) && value.isEmpty()) {
                data.getErrors().put(key, REQUIRED_ERROR_MESSAGE);
                continue;
            }

            if (!value.isEmpty()) {
                Class<?> type = this.getFieldType(key);

                if (!value.isParsed()) {
                    Parser<?> parser = this.getParser(key);

                    if (parser == null) {
                        parser = this.getParser(type);
                    }

                    if (parser == null) {
                        data.getErrors().put(key, PARSER_MISSING_ERROR_MESSAGE);
                        continue;
                    }

                    try {
                        value.setParsed(parser.parse(value));

                    } catch (ParseException e) {
                        data.getErrors().put(key, PARSE_ERROR_MESSAGE);
                        continue;
                    }
                }
            }

            for (Constraint constraint : this.getValidators(key)) {
                Constraint.Result result = constraint.check(value);
                if (!result.isOk()) {
                    data.getErrors().put(key, result.getErrorMessage());
                    break;
                }
            }
        }
    }

    public Set<String> getFieldKeys() {
        return this.fields == null ? Collections.<String>emptySet() : this.fields.keySet();
    }

    public boolean isRequired(String key) {
        return this.required.contains(key);
    }

    public <T> Parser<T> getParser(Class<T> type) {
        return (Parser<T>) this.parsers.get(type);
    }

    public <T> Parser<T> getParser(String key) {
        return (Parser<T>) this.fieldParsers.get(key);
    }

    public Set<Constraint> getValidators(String key) {
        Set<Constraint> constraints = this.constraints.get(key);
        return constraints == null ? Collections.<Constraint>emptySet() : constraints;
    }
}
