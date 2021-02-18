package org.ghotibeaun.json.converters.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.ghotibeaun.json.JSONObject;

public class SetterMethodReference extends MethodReference {

    private final Type parameterType;

    public SetterMethodReference(String name, boolean accessible, Type parameterType, boolean isPublic) {
        super(name, accessible, isPublic);
        this.parameterType = parameterType;
    }

    public Type getParameterType() {
        return parameterType;
    }

    public Class<?> getParameterClass() {
        if (getParameterType() instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType)getParameterType()).getRawType();
        } else {
            return (Class<?>) getParameterType();
        }
    }

    @Override
    public JSONObject getJSON() {
        final JSONObject json = super.getJSON();
        json.put("parameterType", parameterType.toString());
        return json;
    }

}
