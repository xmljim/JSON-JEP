/*
 * Copyright (c) 2019, Flatirons Digital Innovations. All Rights Reserved.
 * This code may not be used without the express written permission
 * of the copyright holder, Flatirons Digital Innovations.
 */
package org.ghotibeaun.json.jsonpath.converter;

import java.util.List;
import java.util.Map;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONObject;

/**
 * @author Jim Earley
 *
 */
public abstract class Converters {

    /**
     *
     */
    private Converters() {
        // TODO Auto-generated constructor stub
    }

    public static Converter<?> getConverter(Class<?> castClass) {
        Converter<?> converter = null;
        if (isPrimitive(castClass)) {
            if (isString(castClass)) {
                converter = new StringConverter();
            } else if (isNumeric(castClass)) {
                converter = new NumberConverter();
            } else if (isBoolean(castClass)) {
                converter = new BooleanConverter();
            }


        } else {
            converter = null;
        }

        return converter;
    }

    private static boolean isPrimitive(Class<?> castClass) {
        return isNumeric(castClass) || isString(castClass) || isBoolean(castClass);
    }

    private static boolean isNumeric(Class<?> castClass) {
        return castClass.getSuperclass().equals(Number.class);
    }

    private static boolean isBoolean(Class<?> castClass) {
        return castClass.getSuperclass().equals(Boolean.class);
    }

    private static boolean isString(Class<?> castClass) {
        return castClass.getSuperclass().equals(String.class);
    }

    @SuppressWarnings("unused")
    private static boolean isList(Class<?> castClass) {
        return implementsInterface(castClass.getInterfaces(), List.class);
    }

    @SuppressWarnings("unused")
    private static boolean isMap(Class<?> castClass) {
        return implementsInterface(castClass.getInterfaces(), Map.class);
    }

    @SuppressWarnings("unused")
    private static boolean isJSONArray(Class<?> castClass) {
        return castClass.equals(JSONArray.class);
    }

    @SuppressWarnings("unused")
    private static boolean isJSONObject(Class<?> castClass) {
        return castClass.equals(JSONObject.class);
    }

    private static boolean implementsInterface(Class<?>[] interfaces, Class<?> interfaceClass) {
        boolean doesImplement = false;

        for (final Class<?> iface : interfaces) {
            if (iface.equals(interfaceClass.getClass())) {
                doesImplement = true;
                break;
            }
        }

        return doesImplement;
    }


}
