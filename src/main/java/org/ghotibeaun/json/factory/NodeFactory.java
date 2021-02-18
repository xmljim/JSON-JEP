package org.ghotibeaun.json.factory;

import java.util.List;
import java.util.Map;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONFactory;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.NullObject;
import org.ghotibeaun.json.converters.Converters;

public final class NodeFactory {


    public static JSONObject newJSONObject() {
        //return new JSONObjectImpl();
        return FactorySettings.createFactoryClass(Setting.OBJECT_CLASS);
    }

    public static JSONArray newJSONArray() {
        //return new JSONArrayImpl();
        return FactorySettings.createFactoryClass(Setting.ARRAY_CLASS);
    }

    public static JSONObject newJSONObject(Map<String, ?> map) {
        return Converters.convertToJSONObject(map);
    }

    public static JSONArray newJSONArray(List<Object> list) {
        return Converters.convertToJSONArray(list);
    }


    public static JSONValue<String> newStringValue(String value) {
        final JSONValue<String> jsonValue = FactorySettings.createFactoryClass(Setting.VALUE_STRING_CLASS);
        jsonValue.setValue(value);
        return jsonValue;

        //return new JSONStringValueImpl(value);
    }

    public static JSONValue<Number> newNumberValue(Number value) {
        final JSONValue<Number> jsonValue = FactorySettings.createFactoryClass(Setting.VALUE_NUMBER_CLASS);
        jsonValue.setValue(value);
        return jsonValue;
        //return new JSONNumberValueImpl(value);
    }



    public static JSONValue<Boolean> newBooleanValue(boolean value) {
        final JSONValue<Boolean> jsonValue = FactorySettings.createFactoryClass(Setting.VALUE_BOOLEAN_CLASS);
        jsonValue.setValue(value);
        return jsonValue;
        //return new JSONBooleanValueImpl(value);
    }

    public static JSONValue<JSONObject> newJSONObjectValue(JSONObject value) {
        final JSONValue<JSONObject> jsonValue = FactorySettings.createFactoryClass(Setting.VALUE_OBJECT_CLASS);
        jsonValue.setValue(value);
        return jsonValue;
        //return new JSONObjectValueImpl(value);
    }


    public static JSONValue<JSONArray> newJSONArrayValue(JSONArray value) {
        final JSONValue<JSONArray> jsonValue = FactorySettings.createFactoryClass(Setting.VALUE_ARRAY_CLASS);
        jsonValue.setValue(value);
        return jsonValue;
        //return new JSONArrayValueImpl(value);
    }


    public static JSONValue<NullObject> newJSONNullValue() {
        return FactorySettings.createFactoryClass(Setting.VALUE_NULL_CLASS);

        //return new JSONNullValueImpl();
    }

    public static JSONNode parse(String data) {
        return JSONFactory.newFactory().newParser().parse(data);
    }




}
