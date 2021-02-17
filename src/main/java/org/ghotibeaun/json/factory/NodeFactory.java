package org.ghotibeaun.json.factory;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONFactory;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.JSONValueType;
import org.ghotibeaun.json.NullObject;
import org.ghotibeaun.json.converters.Converters;
import org.ghotibeaun.json.exception.JSONTypeNotFoundException;
import org.ghotibeaun.json.exception.JSONValueNotFoundException;

public final class NodeFactory {


    public static JSONObject newJSONObject() {
        //return new JSONObjectImpl();
        return FactorySettings.createFactoryClass(FactorySettings.JSON_OBJECT_CLASS);
    }

    public static JSONArray newJSONArray() {
        //return new JSONArrayImpl();
        return FactorySettings.createFactoryClass(FactorySettings.JSON_ARRAY_CLASS);
    }

    public static JSONObject newJSONObject(Map<String, ?> map) {
        return Converters.convertToJSONObject(map);
    }

    public static JSONArray newJSONArray(List<Object> list) {
        return Converters.convertToJSONArray(list);
    }


    public static JSONValue<String> newStringValue(String value) {
        final JSONValue<String> jsonValue = FactorySettings.createFactoryClass(FactorySettings.JSON_VALUE_STRING_CLASS);
        jsonValue.setValue(value);
        return jsonValue;

        //return new JSONStringValueImpl(value);
    }


    public static JSONValue<Number> newNumberValue(Number value) {
        final JSONValue<Number> jsonValue = FactorySettings.createFactoryClass(FactorySettings.JSON_VALUE_NUMBER_CLASS);
        jsonValue.setValue(value);
        return jsonValue;
        //return new JSONNumberValueImpl(value);
    }



    public static JSONValue<Boolean> newBooleanValue(boolean value) {
        final JSONValue<Boolean> jsonValue = FactorySettings.createFactoryClass(FactorySettings.JSON_VALUE_BOOLEAN_CLASS);
        jsonValue.setValue(value);
        return jsonValue;
        //return new JSONBooleanValueImpl(value);
    }

    /**
     * Deprecated
     * @param value
     * @return
     * @deprecated
     */
    @Deprecated(forRemoval = true, since = "2.0.0")
    public static JSONValue<Date> newDateValue(Date value) {

        return new JSONDateValueImpl(value);
    }

    public static JSONValue<JSONObject> newJSONObjectValue(JSONObject value) {
        final JSONValue<JSONObject> jsonValue = FactorySettings.createFactoryClass(FactorySettings.JSON_VALUE_OBJECT_CLASS);
        jsonValue.setValue(value);
        return jsonValue;
        //return new JSONObjectValueImpl(value);
    }


    public static JSONValue<JSONArray> newJSONArrayValue(JSONArray value) {
        final JSONValue<JSONArray> jsonValue = FactorySettings.createFactoryClass(FactorySettings.JSON_VALUE_ARRAY_CLASS);
        jsonValue.setValue(value);
        return jsonValue;
        //return new JSONArrayValueImpl(value);
    }


    public static JSONValue<NullObject> newJSONNullValue() {
        return FactorySettings.createFactoryClass(FactorySettings.JSON_VALUE_NULL_CLASS);

        //return new JSONNullValueImpl();
    }

    /**
     * Creates a JSONValue from an object value;
     * @param o the value
     * @return a JSONValue instance
     * @deprecated Use {@link Converters#convertValue(Object, java.util.Optional, java.util.Optional, org.ghotibeaun.json.converters.options.Options...)}
     */
    @SuppressWarnings("unchecked")
    @Deprecated(forRemoval = true, since = "2.0.0" )
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
        } else {
            v = newStringValue(o.toString());
            //Converters.
        }

        return v;

    }

    /**
     * Creates a JSONValue from an object value;
     * @param source the value
     * @param target the type to cast the value to
     * @return a JSONValue instance
     * @deprecated Use {@link Converters#convertValue(Object, java.util.Optional, java.util.Optional, org.ghotibeaun.json.converters.options.Options...)}
     */
    @Deprecated(forRemoval = true, since = "2.0.0")
    public static JSONValue<?> createFromObject(Object source, Type target) {
        try {
            final Object castedObject = Class.forName(target.getTypeName()).cast(source);
            return createFromObject(castedObject);
        } catch (final ClassNotFoundException e) {
            throw new JSONValueNotFoundException(target.getTypeName(), e);
        }
    }

    public static JSONNode parse(String data) {
        return JSONFactory.newFactory().newParser().parse(data);
    }

    @SuppressWarnings({ "unchecked", "unused" })
    private static Class<JSONNode> createNodeClass(JSONValueType type) {
        Class<JSONNode> clazz = null;

        if (type == JSONValueType.ARRAY) {
            try {
                clazz = (Class<JSONNode>) NodeFactory.class.getClassLoader()
                        .loadClass(FactorySettings.getSetting(FactorySettings.JSON_ARRAY_CLASS));
            } catch (final ClassNotFoundException e) {
                throw new JSONTypeNotFoundException("Type implementation not found for " + FactorySettings.JSON_ARRAY_CLASS,
                        e);
            }
        } else if (type == JSONValueType.OBJECT) {
            try {
                clazz = (Class<JSONNode>) NodeFactory.class.getClassLoader()
                        .loadClass(FactorySettings.getSetting(FactorySettings.JSON_OBJECT_CLASS));
            } catch (final ClassNotFoundException e) {
                throw new JSONTypeNotFoundException("Type implementation not found for " + FactorySettings.JSON_OBJECT_CLASS,
                        e);
            }
        }

        return clazz;
    }

    @SuppressWarnings({ "unchecked", "unused" })
    private static Class<JSONValue<?>> createValueClass(JSONValueType type) {


        Class<JSONValue<?>> clazz = null;

        switch (type) {
            case ARRAY:
                try {
                    clazz = (Class<JSONValue<?>>) NodeFactory.class.getClassLoader()
                            .loadClass(FactorySettings.getSetting(FactorySettings.JSON_VALUE_ARRAY_CLASS));

                } catch (final ClassNotFoundException e) {
                    throw new JSONValueNotFoundException("Value type not found for " + FactorySettings.JSON_VALUE_ARRAY_CLASS, e);
                }

                break;
            case BOOLEAN:
                try {
                    clazz = (Class<JSONValue<?>>) NodeFactory.class.getClassLoader()
                            .loadClass(FactorySettings.getSetting(FactorySettings.JSON_VALUE_BOOLEAN_CLASS));

                } catch (final ClassNotFoundException e) {
                    throw new JSONValueNotFoundException("Value type not found for " + FactorySettings.JSON_VALUE_BOOLEAN_CLASS,
                            e);
                }

                break;
            case DATE:
                try {
                    clazz = (Class<JSONValue<?>>) NodeFactory.class.getClassLoader()
                            .loadClass(FactorySettings.getSetting(FactorySettings.JSON_VALUE_DATE_CLASS));

                } catch (final ClassNotFoundException e) {
                    throw new JSONValueNotFoundException("Value type not found for " + FactorySettings.JSON_VALUE_DATE_CLASS, e);
                }

                break;
            case NULL:
                try {
                    clazz = (Class<JSONValue<?>>) NodeFactory.class.getClassLoader()
                            .loadClass(FactorySettings.getSetting(FactorySettings.JSON_VALUE_NULL_CLASS));

                } catch (final ClassNotFoundException e) {
                    throw new JSONValueNotFoundException("Value type not found for " + FactorySettings.JSON_VALUE_NULL_CLASS, e);
                }

                break;
            case NUMBER:
                try {
                    clazz = (Class<JSONValue<?>>) NodeFactory.class.getClassLoader()
                            .loadClass(FactorySettings.getSetting(FactorySettings.JSON_VALUE_NUMBER_CLASS));

                } catch (final ClassNotFoundException e) {
                    throw new JSONValueNotFoundException("Value type not found for " + FactorySettings.JSON_VALUE_NUMBER_CLASS,
                            e);
                }

                break;
            case OBJECT:
                try {
                    clazz = (Class<JSONValue<?>>) NodeFactory.class.getClassLoader()
                            .loadClass(FactorySettings.getSetting(FactorySettings.JSON_VALUE_OBJECT_CLASS));

                } catch (final ClassNotFoundException e) {
                    throw new JSONValueNotFoundException("Value type not found for " + FactorySettings.JSON_VALUE_OBJECT_CLASS,
                            e);
                }

                break;
            case STRING:
                try {
                    clazz = (Class<JSONValue<?>>) NodeFactory.class.getClassLoader()
                            .loadClass(FactorySettings.getSetting(FactorySettings.JSON_VALUE_STRING_CLASS));

                } catch (final ClassNotFoundException e) {
                    throw new JSONValueNotFoundException("Value type not found for " + FactorySettings.JSON_VALUE_STRING_CLASS,
                            e);
                }

                break;
            default:
                break;

        }

        return clazz;
    }
}
