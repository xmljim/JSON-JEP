[JEP API Guide](index) >> Getting Started

# Getting Started 

## Parsing JSON

To parse JSON, you need obtain a `JSONParser` instance from the `JSONFactory`:

```java
JSONFactory factory = JSONFactory.newFactory() //instantiate the JSONFactory
JSONParser parser = factory.newParser() //

```
## `JSONParser` Methods

The `JSONParser` interface includes several methods for loading a JSON instance:

- `parse(InputStream inputStream)`: parse from an InputStream
- `parse(InputStream inputStream, String charSet)`: parse from an InputStream using a specified Character Set (defaults to UTF-8)
- `parse(URL url)`: parse from a URL
- `parse(String data)`: parse a String JSON instance
- `parse(Reader reader)`: parse from a Reader
- `parse(File file)`: parse from a File
- `parse(Path path)`: parse from a Path instance

In addition, you can also create an empty `JSONObject` or `JSONArray` instance:

- `newJSONObject()`
- `newJSONArray()`

> *Note: There is another way to create empty instances using, `NodeFactory.newJSONObject()` or `NodeFactory.newJSONArray()`, both of which
> are syntactic sugar for creating a `JSONFactory`, creating a new `JSONParser` instance, and invoking the parser's `new*` method.*

### Example Read from an InputStream

```java
JSONFactory factory = JSONFactory.newFactory();
JSONParser parser = factory.newParser();
JSONNode json = null; //JSONNode is the superinterface for both JSONObject and JSONArray

try (InputStream stream = ...) {
    json = parser.parse(stream);
} catch (IOException | JSONParserException e) {
    //do something in the event of an error
}

//we've obtained a JSONNode instance.  Now we can determine the type of Node and convert it:
if (json.isObject()) {
    JSONObject jsonObject = json.asJSONObject();
    //now we have access to all methods of the JSONObject
} else {
    JSONArray jsonArray = json.asJSONArray();
    //work with a JSONArray...
}

```

**Sub-topics**: 

* [Creating an Event Handler](./creating-an-event-handler)
* [Working with the JEP Object Model](./working-with-jep-objects)

**Next**: [Marshalling and Unmarshalling JSON and POJOs](./marshalling-unmarshalling-json-pojo)



