package com.linchproject.validator;

import com.linchproject.validator.parsers.IntegerParser;
import com.linchproject.validator.parsers.StringParser;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Georg Schmidl
 */
public class DataValidator {

    public static String REQUIRED_ERROR = "required";
    public static String PARSER_MISSING_ERROR = "parser.not.found";
    public static String PARSE_ERROR = "invalid.type";

    private Map<String, Class<?>> fields = new LinkedHashMap<String, Class<?>>();

    private Set<String> required = new HashSet<String>();

    private Map<Class<?>, Parser> parsers = new HashMap<Class<?>, Parser>() {{
        put(String.class, new StringParser());
        put(Integer.class, new IntegerParser());
    }};

    private Map<String, Set<Validator>> validators = new HashMap<String, Set<Validator>>();

    public Data createEmptyData() {
        Data data = new Data(this);

        if (this.fields != null) {
            for (String key: this.fields.keySet()) {
                data.add(key);
            }
        }

        return data;
    }

    public Data createDataFrom(Object object) {
        return createEmptyData().readFrom(object);
    }

    public Data createDataFrom(Map<String, String[]> map) {
        return createEmptyData().readFrom(map);
    }

    public DataValidator addField(String key) {
        return this.addFields(key);
    }

    public DataValidator addField(String key, Class<?> type) {
        this.fields.put(key, type);
        return this;
    }

    public DataValidator addFields(String... keys) {
        return addFields(Arrays.asList(keys));
    }

    public DataValidator addFields(Collection<String> keys, String... moreKeys) {
        for (String key: keys) {
            this.fields.put(key, String.class);
        }
        return moreKeys.length > 0 ? addFields(moreKeys) : this;
    }

    public DataValidator addFields(Map<String, Class<?>> fields, String... keys) {
        this.fields.putAll(fields);
        return keys.length > 0 ? addFields(keys) : this;
    }

    public DataValidator addFields(Class<?> clazz, String... keys) {
        for (Method method: clazz.getDeclaredMethods()) {
            if (Reflection.isGetter(method)) {
                String key = Reflection.getNameFromGetter(method.getName());
                Class<?> type = method.getReturnType();
                this.fields.put(key, type);
            }
        }
        return keys.length > 0 ? addFields(keys) : this;
    }

    public DataValidator removeField(String key) {
        return removeFields(key);
    }

    public DataValidator removeFields(String... keys) {
        for (String key: keys) {
            this.fields.remove(key);
        }
        return this;
    }

    public Class<?> getFieldType(String key) {
        return this.fields.get(key);
    }

    public DataValidator setRequired(String key) {
        this.required.add(key);
        return this;
    }

    public DataValidator setRequired(String... keys) {
        Collections.addAll(this.required, keys);
        return this;
    }

    public DataValidator setAllRequired() {
        this.required.addAll(fields.keySet());
        return this;
    }

    public DataValidator addParser(Class<?> type, Parser parser) {
        this.parsers.put(type, parser);
        return this;
    }

    public DataValidator addValidator(String key, Validator validator) {
        return this.addValidators(key, validator);
    }

    public DataValidator addValidators(String key, Validator... validators) {
        if (this.validators.get(key) == null) {
            this.validators.put(key, new HashSet<Validator>());
        }
        Collections.addAll(this.validators.get(key), validators);
        return this;
    }

    public void validate(Data data) {
        data.getErrors().clear();

        for (Map.Entry<String, Value> entry: data.getValues().entrySet()) {
            String key = entry.getKey();
            Value value = entry.getValue();

            if (this.isRequired(key) && value.isEmpty()) {
                data.getErrors().put(key, REQUIRED_ERROR);
                continue;
            }

            if (!value.isEmpty()) {
                Class<?> type = this.getFieldType(key);

                if (!value.isParsed()) {
                    Parser parser = this.getParser(type);
                    if (parser == null) {
                        data.getErrors().put(key, PARSER_MISSING_ERROR);
                        continue;
                    }

                    try {
                        value.setParsed(parser.parse(value));

                    } catch (ParseException e) {
                        data.getErrors().put(key, PARSE_ERROR);
                        continue;
                    }
                }
            }

            for (Validator validator : this.getValidators(key)) {
                if (!validator.isValid(value)) {
                    data.getErrors().put(key, validator.getKey());
                    break;
                }
            }
        }
    }

    public boolean isRequired(String key) {
        return this.required.contains(key);
    }

    public <T> Parser<T> getParser(Class<T> type) {
        return this.parsers.get(type);
    }

    public Set<Validator> getValidators(String key) {
        Set<Validator> validators = this.validators.get(key);
        return validators == null ? Collections.<Validator>emptySet() : validators;
    }
}
