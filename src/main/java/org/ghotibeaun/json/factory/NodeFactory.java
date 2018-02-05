package org.ghotibeaun.json.factory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.JSONValueType;
import org.ghotibeaun.json.NullObject;
import org.ghotibeaun.json.exception.JSONTypeNotFoundException;
import org.ghotibeaun.json.exception.JSONValueNotFoundException;

public final class NodeFactory {


    public static JSONObject newJSONObject() {

        /*        JSONObject o = null;
        final Class<JSONNode> clazz = createNodeClass(JSONValueType.OBJECT);

        try {
            o = (JSONObject) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JSONTypeNotFoundException("Implementation of JSONObject could not be instantiated", e);
        }

        return o;*/
        return new JSONObjectImpl();
    }

    public static JSONArray newJSONArray() {
        /*        JSONArray a = null;
        final Class<JSONNode> clazz = createNodeClass(JSONValueType.ARRAY);
        try {
            a = (JSONArray) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JSONTypeNotFoundException("Implementation of JSONArray could not be instantiated", e);
        }

        return a;*/
        return new JSONArrayImpl();
    }

    public static JSONObject newJSONObject(Map<String, Object> map) {
        final JSONObject obj = newJSONObject();
        final Set<Entry<String, Object>> entries = map.entrySet();

        for (final Entry<String, Object> entry : entries) {
            final String key = entry.getKey();
            final JSONValue<?> value = createFromObject(entry.getValue());
            obj.put(key, value);
        }

        return obj;

    }

    public static JSONArray newJSONArray(List<Object> list) {
        final JSONArray array = newJSONArray();
        for (final Object o : list) {
            array.add(createFromObject(o));
        }
        return array;
    }


    public static JSONValue<String> newStringValue(String value) {
        /*        JSONValue<String> v = null;
        final Class<JSONValue<?>> clazz = createValueClass(JSONValueType.STRING);
        try {
            v = (JSONValue<String>) clazz.newInstance();
            v.setValue(value);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JSONValueNotFoundException("Implementation of String Value could not be instantiated", e);
        }

        return v;*/
        return new JSONStringValueImpl(value);
    }


    public static JSONValue<Number> newNumberValue(Number value) {
        /*        JSONValue<Number> v = null;
        final Class<JSONValue<?>> clazz = createValueClass(JSONValueType.NUMBER);
        try {
            v = (JSONValue<Number>) clazz.newInstance();
            v.setValue(value);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JSONValueNotFoundException("Implementation of Number Value could not be instantiated", e);
        }

        return v;*/
        return new JSONNumberValueImpl(value);
    }



    public static JSONValue<Boolean> newBooleanValue(boolean value) {
        /*        JSONValue<Boolean> v = null;
        final Class<JSONValue<?>> clazz = createValueClass(JSONValueType.BOOLEAN);
        try {
            v = (JSONValue<Boolean>) clazz.newInstance();
            v.setValue(value);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JSONValueNotFoundException("Implementation of Boolean Value could not be instantiated", e);
        }

        return v;*/
        return new JSONBooleanValueImpl(Boolean.valueOf(value));
    }


    public static JSONValue<Date> newDateValue(Date value) {
        /*        JSONValue<Date> v = null;
        final Class<JSONValue<?>> clazz = createValueClass(JSONValueType.DATE);
        try {
            v = (JSONValue<Date>) clazz.newInstance();
            v.setValue(value);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JSONValueNotFoundException("Implementation of Date Value could not be instantiated", e);
        }

        return v;*/
        return new JSONDateValueImpl(value);
    }

    public static JSONValue<JSONObject> newJSONObjectValue(JSONObject value) {
        /*        JSONValue<JSONObject> v = null;
        final Class<JSONValue<?>> clazz = createValueClass(JSONValueType.OBJECT);
        try {
            v = (JSONValue<JSONObject>) clazz.newInstance();
            v.setValue(value);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JSONValueNotFoundException("Implementation of JSONObject Value could not be instantiated", e);
        }

        return v;*/
        return new JSONObjectValueImpl(value);
    }


    public static JSONValue<JSONArray> newJSONArrayValue(JSONArray value) {
        /*        JSONValue<JSONArray> v = null;
        final Class<JSONValue<?>> clazz = createValueClass(JSONValueType.ARRAY);
        try {
            v = (JSONValue<JSONArray>) clazz.newInstance();
            v.setValue(value);
        } catch (InstantiationException | IllegalAccessException e) {

            throw new JSONValueNotFoundException("Implementation of JSONArray Value could not be instantiated", e);
        }

        return v;*/
        return new JSONArrayValueImpl(value);
    }


    public static JSONValue<NullObject> newJSONNullValue() {
        /*        JSONValue<NullObject> v = null;
        final Class<JSONValue<?>> clazz = createValueClass(JSONValueType.NULL);
        try {
            v = (JSONValue<NullObject>) clazz.newInstance();

        } catch (InstantiationException | IllegalAccessException e) {
            throw new JSONValueNotFoundException("Implementation of Null Value could not be instantiated", e);
        }

        return v;*/
        return new JSONNullValueImpl();
    }

    @SuppressWarnings("unchecked")
    public static JSONValue<?> createFromObject(Object o) {

        JSONValue<?> v = null;
        if (o == null) {
            v = newJSONNullValue();
        } else if (o instanceof String) {
            v = newStringValue((String) o);
        } else if (o instanceof Number) {
            v = newNumberValue((Number) o);
        } else if (o instanceof Boolean) {
            v = newBooleanValue((Boolean) o);
        } else if (o instanceof NullObject) {
            v = newJSONNullValue();
        } else if (o instanceof Map) {
            v = newJSONObjectValue(newJSONObject((Map<String, Object>) o));
        } else if (o instanceof List) {
            v = newJSONArrayValue(newJSONArray((List<Object>) o));
        } else if (o instanceof Date) {
            v = newDateValue((Date) o);
        } else if (o instanceof JSONObject) {
            v = newJSONObjectValue((JSONObject)o);
        } else if (o instanceof JSONArray) {
            v = newJSONArrayValue((JSONArray)o);
        }

        return v;

    }

    @SuppressWarnings("unchecked")
    private static Class<JSONNode> createNodeClass(JSONValueType type) {
        Class<JSONNode> clazz = null;

        if (type == JSONValueType.ARRAY) {
            try {
                clazz = (Class<JSONNode>) NodeFactory.class.getClassLoader()
                        .loadClass(FactorySettings.getSetting(FactorySettings.JSON_ARRAY));
            } catch (final ClassNotFoundException e) {
                throw new JSONTypeNotFoundException("Type implementation not found for " + FactorySettings.JSON_ARRAY,
                        e);
            }
        } else if (type == JSONValueType.OBJECT) {
            try {
                clazz = (Class<JSONNode>) NodeFactory.class.getClassLoader()
                        .loadClass(FactorySettings.getSetting(FactorySettings.JSON_OBJECT));
            } catch (final ClassNotFoundException e) {
                throw new JSONTypeNotFoundException("Type implementation not found for " + FactorySettings.JSON_OBJECT,
                        e);
            }
        }

        return clazz;
    }

    @SuppressWarnings("unchecked")
    private static Class<JSONValue<?>> createValueClass(JSONValueType type) {


        Class<JSONValue<?>> clazz = null;

        switch (type) {
        case ARRAY:
            try {
                clazz = (Class<JSONValue<?>>) NodeFactory.class.getClassLoader()
                        .loadClass(FactorySettings.getSetting(FactorySettings.JSON_VALUE_ARRAY));

            } catch (final ClassNotFoundException e) {
                throw new JSONValueNotFoundException("Value type not found for " + FactorySettings.JSON_VALUE_ARRAY, e);
            }

            break;
        case BOOLEAN:
            try {
                clazz = (Class<JSONValue<?>>) NodeFactory.class.getClassLoader()
                        .loadClass(FactorySettings.getSetting(FactorySettings.JSON_VALUE_BOOLEAN));

            } catch (final ClassNotFoundException e) {
                throw new JSONValueNotFoundException("Value type not found for " + FactorySettings.JSON_VALUE_BOOLEAN,
                        e);
            }

            break;
        case DATE:
            try {
                clazz = (Class<JSONValue<?>>) NodeFactory.class.getClassLoader()
                        .loadClass(FactorySettings.getSetting(FactorySettings.JSON_VALUE_DATE));

            } catch (final ClassNotFoundException e) {
                throw new JSONValueNotFoundException("Value type not found for " + FactorySettings.JSON_VALUE_DATE, e);
            }

            break;
        case NULL:
            try {
                clazz = (Class<JSONValue<?>>) NodeFactory.class.getClassLoader()
                        .loadClass(FactorySettings.getSetting(FactorySettings.JSON_VALUE_NULL));

            } catch (final ClassNotFoundException e) {
                throw new JSONValueNotFoundException("Value type not found for " + FactorySettings.JSON_VALUE_NULL, e);
            }

            break;
        case NUMBER:
            try {
                clazz = (Class<JSONValue<?>>) NodeFactory.class.getClassLoader()
                        .loadClass(FactorySettings.getSetting(FactorySettings.JSON_VALUE_NUMBER));

            } catch (final ClassNotFoundException e) {
                throw new JSONValueNotFoundException("Value type not found for " + FactorySettings.JSON_VALUE_NUMBER,
                        e);
            }

            break;
        case OBJECT:
            try {
                clazz = (Class<JSONValue<?>>) NodeFactory.class.getClassLoader()
                        .loadClass(FactorySettings.getSetting(FactorySettings.JSON_VALUE_OBJECT));

            } catch (final ClassNotFoundException e) {
                throw new JSONValueNotFoundException("Value type not found for " + FactorySettings.JSON_VALUE_OBJECT,
                        e);
            }

            break;
        case STRING:
            try {
                clazz = (Class<JSONValue<?>>) NodeFactory.class.getClassLoader()
                        .loadClass(FactorySettings.getSetting(FactorySettings.JSON_VALUE_STRING));

            } catch (final ClassNotFoundException e) {
                throw new JSONValueNotFoundException("Value type not found for " + FactorySettings.JSON_VALUE_STRING,
                        e);
            }

            break;
        default:
            break;

        }

        return clazz;
    }
}
