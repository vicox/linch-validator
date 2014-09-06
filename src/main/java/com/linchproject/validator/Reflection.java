package com.linchproject.validator;

import java.lang.reflect.Method;

/**
 * @author Georg Schmidl
 */
public class Reflection {

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
        if (setterName.length() > 4) {
            name += setterName.substring(4, setterName.length());
        }
        return name;
    }

    public static String getNameFromGetter(String getterName) {
        String name = getterName.substring(3, 4).toLowerCase();
        if (getterName.length() > 4) {
            name += getterName.substring(4, getterName.length());
        }
        return name;
    }
}
