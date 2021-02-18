package org.ghotibeaun.json.converters.utils;

import java.lang.reflect.Type;

import org.ghotibeaun.json.JSONObject;

public class GetterMethodReference extends MethodReference {

    private final Type returnType;

    public GetterMethodReference(String name, boolean accessible, Type returnType, boolean isPublic) {
        super(name, accessible, isPublic);
        this.returnType = returnType;
    }

    public Type getReturnType() {
        return returnType;
    }

    @Override
    public JSONObject getJSON() {
        final JSONObject json = super.getJSON();
        json.put("returnType", returnType.toString());
        return json;
    }

}
