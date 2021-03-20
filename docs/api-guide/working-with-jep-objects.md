[JEP API Guide](index) >> [Getting Started](getting-started) >> Working with the JEP Object Model

# Working with the JEP Object Model

The JEP Object model is defined from four key interfaces:

* [`JSONNode`](../javadocs/org/ghotibeaun/json/JSONNode.html) - This is the tagging base interface that all JSON Object and JSON Array classes inherit from
* [`JSONObject`](../javadocs/org/ghotibeaun/json/JSONObject.html) - Represents a JSON object (data wrapped in `{` and `}` in JSON notation)
* [`JSONArray`](../javadocs/org/ghotibeaun/json/JSONArray.html) - Represents a JSON array (data wrapped in `[` and `]` in JSON notation)
* [`JSONValue<T>`](../javadocs/org/ghotibeaun/json/JSONValue.html) - Represents a value contained in either `JSONObject` or `JSONArray`. The generic interface allows for any value to be represented.  

The `JSONObject` and `JSONArray` interfaces include numerous setter and getter methods that allow you to create JSON objects easily. 

Pulling these together are a set of Factory classes.  These classes contain static members that allow you to quickly and easily create instances of these interfaces.  The are two main Factory classes in the JOM implementation:

* [`JSONFactory`](../javadocs/org/ghotibeaun/json/JSONFactory.html) - This is the entry point for creating JSON objects, Parsers, Converters, and Serializers
* [`NodeFactory`](../javadocs/org/ghotibeaun/json/factory/NodeFactory.html) - contains methods that allow you to create concrete implementations `JSONObject`, `JSONArray` and various `JSONValue` instances.  There are other helper methods that will process `java.util.List` and `java.util.Map` instances into JSON. 
  Likewise there are methods that will convert `java.lang.Object` instances into values recognized by JSON.

## JSONFactory
The JSONFactory is a primary entry point to most features within JSON-JEP, including Parsers, Serializers, Converters and JSONPath. While
most of these components can be instantiated from various other factory classes in the API, this provides a single entry point for these features:

```java
//create a new JSONFactory
JSONFactory factory = JSONFactory.newFactory();

//create a JSONParser instance
JSONParser parser = factory.newParser();

//create a JSONCSVParser instance
JSONCSVParser csvParser = factory.newCsvParser();

//create a JSONSerializer instance
JSONSerializer serializer = factory.newSerializer();

//create an XMLSerializer instance
XMLSerializer xmlSerializer = factory.newXMLSerializer();

//create a JSONPath instance
JSONPath jsonPath = factory.newJSONPath("path.to.item");

//create a JSONConverter (unmarshal JSON to a POJO)
JSONConverter jsonConverter = factory.newJSONConverter();

//create a ClassConverter (marshal a POJO to JSON)
ClassConverter classConverter = factory.newClassConverter();

```

## Working with a Parser

The `JSONParser` interface defines numerous methods for parsing and loading a JSON instance from 
various input sources. Use the `JSONFactory.newParser()` to create a new instance. Internally,
the `JSONFactory` implementation calls [`FactorySettings`](./factory-settings) to locate the defined `JSONParser`
implementation class and create a new instance. 

```java
//Create a Factory instance
JSONFactory factory = JSONFactory.newFactory();

//Create a new JSONParser instance
JSONParser parser = factory.newParser();
``` 

## The JSONNode Interface

The `JSONNode` interface is the superinterface for `JSONObject` and `JSONArray` interfaces, and contains methods
common to both. It also contains methods to provide hints for the underlying type, and for casting to that type. There 
are other convenience methods to support other features such as serialization, and JSONPath queries directly from the JSONNode instance. 

```java
//Parse JSON data - returns a JSONNode instance

JSONFactory factory = JSONFactory.newFactory();
JSONParser parser = factory.newParser();
JSONNode result = parser.parse(...);

//now that we have a JSONNode, we can evaluate what type it is, and cast it:
if (result.isObject()) {
    JSONObject resultObject = result.asJSONObject();
    //now we can work with it
} else if (result.isArray()) {
    JSONArray resultArray = result.asJSONArray();
    //work with the array
}
```

## The JSONObject Interface

The `JSONObject` interface represents an associative array of key/value pairs. It inherits all
methods from `JSONNode` and from `JSONMapNode` (which contains more abstract methods for managing key/value entries).
You use the `put(String,...)` methods for adding/or changing entries, and one of the `get***` methods to return a
value from a specific key:

```java
JSONObject newObject = NodeFactory.newJSONObject();

//Add a String value:
newObject.put("string-1", "A new String value");

//add a boolean value
newObject.put("booleanValue", true);

//add a null value
newObject.putNull("nullValue");

//add a number
newObject.put("numberValue", 1234);

//add a JSONObject value:
JSONObject subObject = NodeFactory.newJSONObject();
subObject.put("name", "Jim Earley");
subObject.put("email", "xml.jim@gmail.com");

newObject.put("subObject", subObject);

//add a JSONArray value:
JSONArray array = NodeFactory.newJSONArray();
array.add("foo");
array.add("bar");

newObject.put("array", array);

```

Using the `prettyPrint()` method, I can return the current JSON instance as a String:

```json
{
    "string-1": "A new String value",
    "booleanValue": true,
    "nullValue": null,
    "numberValue": 1234,
    "subObject": {
        "name": "Jim Earley",
        "email": "xml.jim@gmail.com"
    },
    "array": [
        "foo",
        "bar"
    ]
}
```

To access values in the JSONObject instance, you can use one of the `get***` methods:

```java
//get the string value
String stringValue = newObject.getString("string-1");

//get the boolean value
boolean booleanValue = newObject.getBoolean("booleanValue");

//get the null value by using Object as the base type
//we'll use 'getValue' which can be used for any value type
Object nullValue = newObject.getValue("nullValue");

//return a number - we could choose getInt, or getLong
int numberValue = newObject.getInt("numberValue");

//return a JSONObject
JSONObject subObject = newObject.getJSONObject("subObject");

//return a JSONArray
JSONArray array = newObject.getJSONArray("array");
```

The methods above work well if we know exactly what the underlying data types are for each property key. 
For those cases where we're not sure what the value type is, we can use the `getValueType(String)` method to 
give us a hint. `getValueType` returns a [JSONValueType](../javadocs/org/ghotibeaun/json/JSONValueType.html) value that 
tells us the value it is holding and determine how to access it.

```java
JSONValueType valueType = newObject.getValueType("string-1"); //returns STRING

valueType = newObject.getValueType("booleanValue"); //returns BOOLEAN
valueType = newObject.getValueType("nullValue"); //returns NULL
valueType = newObject.getValueType("numberValue"); //returns INTEGER or NUMBER

valueType = newObject.getValueType("subObject"); //returns OBJECT
valueType = newObject.getValueType("array"); //returns ARRAY
```

### Bulk Put operations

There are cases where you might want to bulk add more than one key/value pair at a time. You can use the
`putAllRaw(Map<String, Object>)` method. This allows you use a standard [Map](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Map.html) interface
to bind the values to a JSONObject.

```java
Map<String, Object> standardMap = new HashMap<>();
standardMap.put("string","a string value");
standardMap.put("boolean", true);

JSONObject mapObject = NodeFactory.newJSONObject();
mapObject.putAllRaw(standardMap);

```

### Using a JSONObject as a Map

There are scenarios that might require that you are using a standard Map interface in some other part of your
application. The `getMap()` method will create and return a `Map<String, Object>` instance. 

```java
Map<String, Object> mapReference = myJSONObject.getMap();
```

### Looping/Iterating through a JSONObject

A common scenario might be that you want to traverse a JSONObject to process each entry.  There are several methods that you could use

* `elements()`: Returns a `Set<Entry<String, JSONValue<?>>>` of every key/value pair. Values are stored as `JSONValue<?>` instances 
  (we'll touch on these further down).
* `keys()`: Returns an `Iterable<String>` of key values.
