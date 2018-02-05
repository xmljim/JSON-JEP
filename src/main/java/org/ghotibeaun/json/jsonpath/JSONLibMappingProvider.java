package org.ghotibeaun.json.jsonpath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.JSONValueType;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

public class JSONLibMappingProvider implements MappingProvider {

    public JSONLibMappingProvider() {
        // TODO Auto-generated constructor stub
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T map(Object source, Class<T> targetType, Configuration configuration) {
        if (source == null) {
            return null;
        }

        if (targetType.equals(Object.class)) {
            return (T) mapValueToObject(source);
        }

        if (targetType.equals(Map.class)) {
            return (T) mapObjectToObject(source);
        }

        if (targetType.equals(List.class)) {
            return (T) mapArrayToObject(source);
        }

        return null;
    }

    @Override
    public <T> T map(Object source, TypeRef<T> targetType, Configuration configuration) {
        // TODO Auto-generated method stub
        return null;
    }

    private Object mapObjectToObject(Object source) {
        final Map<String,Object> mapped = new HashMap<String, Object>();

        final JSONObject obj = (JSONObject)source;
        for (final Entry<String, JSONValue<?>> o : obj.elements()) {
            mapped.put(o.getKey(), mapValueToObject(o.getValue()));
        }

        return mapped;
    }

    private Object mapArrayToObject(Object source) {
        final List<Object> mapped = new ArrayList<Object>();

        final JSONArray arr = (JSONArray)source;

        for (final JSONValue<?> val : arr.getValues()) {
            mapped.add(mapValueToObject(val));
        }

        return mapped;
    }

    private Object mapValueToObject(Object source) {
        final JSONValue<?> val = (JSONValue<?>)source;

        if (val.getType().equals(JSONValueType.ARRAY)) {
            return mapArrayToObject(val.getValue());
        } else if (val.getType().equals(JSONValueType.OBJECT)) {
            return mapObjectToObject(val.getValue());
        } else if (val.getType().equals(JSONValueType.NULL)) {
            return null;
        } else {
            return val.getValue();
        }


    }

}
