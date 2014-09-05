package com.linchproject.validator;

import java.lang.reflect.Method;

/**
 * @author Georg Schmidl
 */
public class Reflection {

    public static Class<?> getFieldClass(Class<?> clazz, String fieldName) {
        Class<?> fieldClass = null;

        try {
            Method getter = clazz.getMethod(getGetterName(fieldName));
            fieldClass = getter.getReturnType();
        } catch (NoSuchMethodException e) {
            // ignore
        }

        return fieldClass;
    }

    public static String getSetterName(String name) {
        String setterName = "set" + name.substring(0, 1).toUpperCase();
        if (name.length() > 1) {
            setterName += name.substring(1, name.length());
        }
        return  setterName;
    }

    public static String getGetterName(String name) {
        String getterName = "get" + name.substring(0, 1).toUpperCase();
        if (name.length() > 1) {
            getterName += name.substring(1, name.length());
        }
        return  getterName;
    }

    public static boolean isSetter(Method method) {
        return method.getName().startsWith("set")
                && method.getName().length() > 3
                && method.getParameterTypes().length == 1;
    }

    public static boolean isGetter(Method method) {
        return method.getName().startsWith("get")
                && method.getName().length() > 3
                && method.getParameterTypes().length == 0;
    }

    public static String getNameFromSetter(String setterName) {
        String name = setterName.substring(3, 4).toLowerCase();
        if (setterName.length() > 5) {
            name += setterName.substring(5, setterName.length());
        }
        return name;
    }

    public static String getNameFromGetter(String getterName) {
        String name = getterName.substring(3, 4).toLowerCase();
        if (getterName.length() > 5) {
            name += getterName.substring(5, getterName.length());
        }
        return name;
    }
}
