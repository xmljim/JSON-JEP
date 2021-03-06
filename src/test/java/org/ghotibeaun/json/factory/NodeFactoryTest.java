package org.ghotibeaun.json.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.JSONValueType;
import org.ghotibeaun.json.NullObject;
import org.ghotibeaun.json.converters.Converters;
import org.junit.Test;

public class NodeFactoryTest {

    @Test
    public void testNewJSONObject() {
        final JSONObject obj = NodeFactory.newJSONObject();
        assertNotNull(obj);
    }

    @Test
    public void testNewJSONArray() {
        final JSONArray arr = NodeFactory.newJSONArray();
        assertNotNull(arr);
    }

    @Test
    public void testNewJSONObjectMapOfStringObject() {
        final Map<String, Object> testMap = new HashMap<>();

        testMap.put("stringKey", "StringValue");
        testMap.put("booleanKey", false);
        testMap.put("longKey", 1000L);
        testMap.put("decimalKey", 3.1415927D);
        testMap.put("floatKey", 1.610339887F);
        testMap.put("nullValue", null);


        final ArrayList<Object> arr = new ArrayList<>();
        arr.add("StringValue");
        arr.add(true);
        arr.add(200000000L);
        arr.add(3.1415927D);
        arr.add(1.610339887F);
        arr.add(null);

        testMap.put("arrayValue", arr);

        final Map<String, Object> secondMap = new HashMap<>();
        secondMap.put("anotherKey", "value");

        testMap.put("mapValue", secondMap);

        final JSONObject obj = NodeFactory.newJSONObject(testMap);

        System.out.println(obj.prettyPrint());
        assertNotNull(obj);
        assertTrue(obj.getLong("longKey") == 1000L);

    }

    @Test
    public void testNewJSONArrayListOfObject() {
        final ArrayList<Object> arr = new ArrayList<>();
        arr.add("StringValue");
        arr.add(true);
        arr.add(200000000L);
        arr.add(3.1415927D);
        arr.add(1.610339887F);
        arr.add(null);

        final JSONArray v = NodeFactory.newJSONArray(arr);
        assertTrue((boolean)arr.get(1) == v.getBoolean(1));
        assertTrue(((String)arr.get(0)).equals(v.getString(0)));
    }

    @Test
    public void testNewStringValue() {
        final String s = "Hello World";
        final JSONValue<String> v = NodeFactory.newStringValue(s);
        assertTrue(s.equals(v.getValue()));
    }

    @Test
    public void testNewNumberValue() {
        final JSONValue<Number> v = NodeFactory.newNumberValue(3.1415927);
        assertTrue(3.1415927 == v.getValue().doubleValue());
    }

    @Test
    public void testNewBooleanValue() {

        final boolean b = true;
        final JSONValue<Boolean> v = NodeFactory.newBooleanValue(b);
        assertTrue(b == v.getValue());
    }

    @Test
    public void testNewJSONObjectValue() {
        final Map<String, Object> testMap = new HashMap<>();
        testMap.put("stringKey", "StringValue");
        testMap.put("booleanKey", false);
        testMap.put("longKey", 1000L);
        testMap.put("decimalKey", 3.1415927D);
        testMap.put("floatKey", 1.610339887F);
        testMap.put("nullValue", null);

        final JSONObject o = NodeFactory.newJSONObject(testMap);

        final JSONValue<JSONObject> v = NodeFactory.newJSONObjectValue(o);
        assertTrue(v.getType() == JSONValueType.OBJECT);
        assertTrue(testMap.get("stringKey").toString().equals(v.getValue().getString("stringKey")));
        assertTrue(testMap.get("nullValue") == v.getValue().get("nullValue").getValue());


    }

    @Test
    public void testNewJSONArrayValue() {
        final List<Object> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");

        final JSONArray arr = NodeFactory.newJSONArray(list);

        final JSONValue<JSONArray> v = NodeFactory.newJSONArrayValue(arr);

        assertTrue(list.size() == v.getValue().size());

        for (int i = 0; i < list.size(); i++) {
            assertTrue("Test list match [" + i + "] " + list.get(i).toString() + "=" + v.getValue().get(i).toString(), list.get(i).toString().equals(v.getValue().get(i).getValue()));
        }
    }

    @Test
    public void testNewJSONNullValue() {
        final JSONValue<NullObject> v = NodeFactory.newJSONNullValue();
        assertNull(v.getValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateFromObject() {
        final JSONValue<?> bool = Converters.convertToJSONValue(Boolean.TRUE, Optional.empty(), Optional.empty());//NodeFactory.createFromObject(Boolean.TRUE);
        assertTrue(bool.getType() == JSONValueType.BOOLEAN);
        assertTrue(((JSONValue<Boolean>)bool).getValue() == true);

        final JSONValue<?> nul = Converters.convertToJSONValue(null, Optional.empty(), Optional.empty());
        assertTrue(nul.getType() == JSONValueType.NULL);
        assertTrue(((JSONValue<NullObject>)nul).getValue() == null);
    }

}
