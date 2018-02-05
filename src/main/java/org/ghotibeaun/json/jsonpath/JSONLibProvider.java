package org.ghotibeaun.json.jsonpath;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONFactory;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.JSONValueType;
import org.ghotibeaun.json.exception.JSONInvalidValueTypeException;
import org.ghotibeaun.json.exception.JSONParserException;
import org.ghotibeaun.json.factory.NodeFactory;

import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.spi.json.AbstractJsonProvider;

public class JSONLibProvider extends AbstractJsonProvider {
    
    @Override
    public Object getArrayIndex(Object obj, int idx) {
        return toJSONArray(obj).get(idx).getValue();
    }
    
    @Override
    public Object getMapValue(Object obj, String key) {
        if (obj instanceof JSONObject) {
            return toJSONObject(obj).get(key).getValue();
        } else if (obj instanceof JSONValue) {
            @SuppressWarnings("unchecked")
            final JSONValue<JSONObject> val = (JSONValue<JSONObject>) toJSONValue(obj);
            if (val.getValue().containsKey(key)) {
                return val.getValue().get(key).getValue();
            } else {
                return null;
            }
            
        } else {
            return obj;
        }
    }
    
    @Override
    public Collection<String> getPropertyKeys(Object obj) {
        final ArrayList<String> keys = new ArrayList<>();
        
        if (obj instanceof JSONObject) {
            for (final String key : toJSONObject(obj).keys()) {
                keys.add(key);
            }
        } else {
            @SuppressWarnings("unchecked")
            final JSONValue<JSONObject> map = (JSONValue<JSONObject>)obj;
            for (final String key : map.getValue().keys()) {
                keys.add(key);
            }
        }
        return keys;
    }
    
    @Override
    public boolean isArray(Object obj) {
        boolean isArray = false;
        isArray = (obj instanceof JSONArray) || (obj instanceof List);
        
        if (!isArray) {
            if (obj instanceof JSONValue) {
                isArray = toJSONValue(obj).getType() == JSONValueType.ARRAY;
            }
        }
        
        return isArray;
    }
    
    @Override
    public boolean isMap(Object obj) {
        boolean isMap = false;
        isMap = (obj instanceof JSONObject) || (obj instanceof Map);
        
        if (!isMap) {
            if (obj instanceof JSONValue) {
                isMap = toJSONValue(obj).getType() == JSONValueType.OBJECT;
            }
        }
        
        return isMap;
    }
    
    @Override
    public int length(Object obj) {
        if (isArray(obj)) {
            return toJSONArray(obj).size();
        } else if (isMap(obj)) {
            return toJSONObject(obj).size();
        } else if (obj == null) {
            return 0;
        } else {
            return 1;
        }
    }
    
    @Override
    public void removeProperty(Object arg0, Object arg1) {
        // TODO Auto-generated method stub
        super.removeProperty(arg0, arg1);
    }
    
    @Override
    public void setArrayIndex(Object source, int index, Object value) {
        ((JSONArray) source).insert(index, value);
    }
    
    @Override
    public void setProperty(Object source, Object key, Object value) {
        ((JSONObject) source).put(key.toString(), value);
    }
    
    @Override
    public Iterable<? extends Object> toIterable(Object obj) {
        if (obj instanceof JSONObject) {
            return ((JSONObject) obj).elements();
        } else if (obj instanceof JSONArray) {
            return ((JSONArray) obj);
        } else {
            return null;
        }
    }
    
    @Override
    public Object unwrap(Object obj) {
        if (obj instanceof JSONValue) {
            return ((JSONValue<?>) obj).getValue();
        } else {
            return obj;
        }
    }
    
    public JSONLibProvider() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public Object createArray() {
        return NodeFactory.newJSONArray();
    }
    
    @Override
    public Object createMap() {
        return NodeFactory.newJSONObject();
    }
    
    @Override
    public Object parse(String data) throws InvalidJsonException {
        return JSONFactory.newFactory().newParser().parse(data);
    }
    
    @Override
    public Object parse(InputStream inputStream, String charSet) throws InvalidJsonException {
        JSONNode result = null;
        
        try {
            result = JSONFactory.newFactory().newParser().parse(inputStream, charSet);
        } catch (final JSONParserException e) {
            try {
                result = JSONFactory.newFactory().newParser().parse(inputStream, charSet);
            } catch (final JSONParserException e1) {
                throw new InvalidJsonException(e1);
            }
        }
        
        return result;
    }
    
    @Override
    public String toJson(Object o) {
        return ((JSONNode) o).toJSONString();
    }
    
    @SuppressWarnings("unchecked")
    private JSONArray toJSONArray(Object o) {
        JSONArray jsonArray = null;
        
        if (o instanceof JSONArray) {
            jsonArray = (JSONArray) o;
        } else if (o instanceof JSONValue) {
            final JSONValue<JSONArray> arrVal = (JSONValue<JSONArray>) o;
            jsonArray = arrVal.getValue();
        } else {
            throw new JSONInvalidValueTypeException("Object is not a JSONArray instance");
        }
        return jsonArray;
    }
    
    private JSONObject toJSONObject(Object o) {
        return (JSONObject) o;
    }
    
    private JSONValue<?> toJSONValue(Object o) {
        return (JSONValue<?>) o;
    }
    
}
