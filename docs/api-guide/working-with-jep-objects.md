[JEP API Guide](index.html) >> [Getting Started](getting-started.html) >> Working with the JEP Object Model

# Working with the JEP Object Model

The JEP Object model is defined from four key interfaces:

* [`JSONNode`](../javadocs/org/ghotibeaun/json/JSONNode.html) - This is the tagging base interface that all JSON Object and JSON Array classes inherit from
* [`JSONObject`](../javadocs/org/ghotibeaun/json/JSONObject.html) - Represents a JSON object (data wrapped in `{` and `}` in JSON notation)
* [`JSONArray`](../javadocs/org/ghotibeaun/json/JSONArray.html) - Represents a JSON array (data wrapped in `[` and `]` in JSON notation)
* [`JSONValue<T>`](../javadocs/org/ghotibeaun/json/JSONValue.html) - Represents a value contained in either `JSONObject` or `JSONArray`. The generic interface allows for any value to be represented.  

The `JSONObject` and `JSONArray` interfaces include numerous setter and getter methods that allow you to create JSON objects easily. 

Pulling these together are a set of Factory classes.  These classes contain static members that allow you to quickly and easily create instances of these interfaces.  The are two main Factory classes in the JOM implementation:

* [`JSONFactory`](../javadocs/org/ghotibeaun/json/JSONFactory.html) - This is the entry point for creating JSON objects and Parsers
* [`NodeFactory`](../javadocs/org/ghotibeaun/json/factory/NodeFactory.html) - contains methods that allow you to create concrete implementations `JSONObject`, `JSONArray` and various `JSONValue` instances.  There are other helper methods that will process `java.util.List` and `java.util.Map` instances into JSON.  Likewise there are methods that will convert `java.lang.Object` instances into values recognized by the JOM.