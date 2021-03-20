[JEP API Guide](./index) >> [Getting Started](./getting-started) >> Factory Settings

# Factory Settings

JEP is configured through a set of properties to support various parts of the API.

The [FactorySettings](../javadocs/org/ghotibeaun/json/factory/FactorySettings.html) class manages all settings for features like the default
JSONParser implementation, serialization, Converters, JSONPath and so on. Settings are typically used by various factory classes in the API, 
However, other classes have access to these properties if needed. When JEP first loads, the `FactorySettings` class initializes with a default 
set of properties.  However, you can change these default settings.

Settings can be applied either by using the property name, or more conveniently through the [Setting](../javadocs/org/ghotibeaun/json/factory/Setting.html)
enum. 

```java
    //This setting...
    FactorySettings.applySetting("org.ghotibeaun.json.merge.appendkey", "_merge");
    
    //is equivalent to this one...
    FactorySettings.applySetting(Setting.MERGE_APPEND_KEY, "_merge");
```




The `FactorySettings` class includes several methods for accessing each setting.  By default, `getSetting` will return the most current
value for a given property.  However, you can always default to a specific setting using the static `getDefaultSetting` or `getCustomSetting` methods.
`getDefaultSetting` is the syntactic equivalent to `getSetting(Setting, true)`. Conversely `getCustomSetting` is the equivalent of
`getSetting(Setting, false)`.

```java
FactorySettings.applySetting(Setting.MERGE_APPEND_KEY, "-merge");

String currentAppend = FactorySettings.getSetting(Setting.MERGE_APPEND_KEY);
//returns "-merge". Current will use custom value first, if available, then fall back to the default value if the custom value is not set


String defaultAppend = FactorySettings.getDefaultSetting(Setting.MERGE_APPEND_KEY);
//returns "_append"
//could also retrieve value using getSetting(Setting.MERGE_APPEND_KEY, true)

String customAppend = FactorySettings.getCustomSetting(Setting.MERGE_APPEND_KEY)
//return "-merge"
//could also retrieve value using getSetting(Setting.MERGE_APPEND_KEY, false)
```

## Class Settings
Class settings are used to configure implementation class for JEP features like event parsing, JSONPath,
[Converters](marshalling-unmarshalling-json-pojo), and Serializers. To apply or return the class, you can
use the following methods.

* `static void applySetting(Setting setting, Class<?> clazz)`: set a class reference to a specific setting
* `static Class<?> getClassSetting(Setting setting)`: return a class reference from a setting.

> **NOTE**: There are no internal checks to validate that a class reference is appropriate for a given setting

> **NOTE**: You can also use the `applySetting(Setting, String)` method, if the setting is known to be a class setting.
> Class settings are identified with `_CLASS` suffix on each `Setting` enum constant. 

### Example

Let's assume you are using JEP with a custom EventHandler implementation. You can define your event handler before parsing data:

```java
//Set up my event handler:
FactorySettings.applySetting(Setting.EVENT_HANDLER_CLASS, MyEventHandler.class);

//alternately you can do the same with the fully qualified class name String:
FactorySettings.applySetting(Setting.EVENT_HANDLER_CLASS, "com.example.MyEventHandler");

//Now parse the data
JSONFactory factory = JSONFactory.newFactory();
JSONParser parser = factory.newParser(); //The parser is initialized with your event handler

JSONNode result = parser.parse(theInputStream);

```

Since setting are "sticky," every time you invoke a `parse`, your event handler is used. If you want to return to the
default setting, you can use the `setUseDefaultSettings(true)` method:

```java
FactorySettings.setUseDefaultSettings(true); //use the default configuration

//Now if we run parse, the default event handler is used:
JSONFactory factory = JSONFactory.newFactory();
JSONParser parser = factory.newParser(); //The parser is initialized with the default handler

JSONNode result = parser.parse(theInputStream);
```

Settings can be applied or retrieved by using the setting name, or using a specific [Setting](../javadocs/org/ghotibeaun/json/factory/Setting.html) enum constant:

## Properties

| Property Name                        | Description                                                          | [Setting](../javadocs/org/ghotibeaun/json/factory/Setting.html) | Default Value |
|--------------------------------------|----------------------------------------------------------------------|--------------------|---------------|
|`org.ghotibeaun.json.convert.classconverter` | Specifies the [ClassConverter](../javadocs/org/ghotibeaun/json/converters/ClassConverter.html) implementation class | `CLASS_CONVERTER_CLASS` | _internal class reference_ |
|`org.ghotibeaun.json.convert.jsonconverter` | Specifies the [JSONConverter](../javadocs/org/ghotibeaun/json/converters/JSONConverter.html) implementation class | `JSON_CONVERTER_CLASS` | _internal class reference_ |
|`org.ghotibeaun.json.converter.option.ignorekeys` | Specifies keys to ignore during a conversion. Values should be comma separated | `CONVERTER_IGNORE_KEYS` | _EMPTY STRING_ |
|`org.ghotibeaun.json.converter.option.keycase` | Specifies the key case for mapping JSON properties to POJO fields/methods. Maps to a [KeyNameCasing](../javadocs/org/ghotibeaun/json/converters/options/KeyNameCasing.html) enum value | `CONVERTER_JSON_KEY_CASE` | `CAMEL`|
|`org.ghotibeaun.json.converter.option.validation` | Specifies the validation option when scanning POJO classes.  Maps to a [ScannerValidationOption](../javadocs/org/ghotibeaun/json/converters/options/ScannerValidationOption.html) enum value | `CONVERTER_VALIDATION` | `LAX` |
|`org.ghotibeaun.json.date.format` | Specifies the default date format for use by CSV to JSON handlers | `DATE_FORMAT` | "`yyyy-MM-dd'T'HH:mm:ss.SSSZ`" |
|`org.ghotibeaun.json.event.handler` | Specifies the [JSONEventHandler](../javadocs/org/ghotibeaun/json/parser/jep/eventhandler/JSONEventHandler.html) implementation class | `EVENT_HANDLER_CLASS` | _internal class reference_ |
|`org.ghotibeaun.json.event.parser`    | Specifies the [JSONEventParser](../javadocs/org/ghotibeaun/json/parser/jep/JSONEventParser.html) implementation class | `EVENT_PARSER_CLASS` | _internal class reference_ |
|`org.ghotibeaun.json.event.processor` | Specifies the [JSONEventProcessor](../javadocs/org/ghotibeaun/json/parser/jep/processor/JSONEventProcessor.html) implementation class | `EVENT_PROCESSOR_CLASS` | _internal class reference_ |
|`org.ghotibeaun.json.event.provider`  | Specifies the [JSONEventProvider](../javadocs/org/ghotibeaun/json/parser/jep/eventprovider/JSONEventProvider.html) implementation class | `EVENT_PROVIDER_CLASS` | _internal class reference_ |
|`org.ghotibeaun.json.factory`         | Specifies the underlying implementation class for the `JSONFactory` | `FACTORY_CLASS` | _internal class reference_ |
|`org.ghotibeaun.json.inputstream.charset` | Specifies the character set encoding to use for parsing JSON | `INPUTSTREAM_CHARSET` | "`UTF-8`" |
|`org.ghotibeaun.json.jsonpath` | Specifies the [JSONPath](../javadocs/org/ghotibeaun/json/jsonpath/JSONPath.html) implementation class | `JSONPATH_IMPL_CLASS` | _internal class reference_ |
|`org.ghotibeaun.json.jsonpath.jsonprovider` | Specifies the `com.jayway.jsonpath.spi.json.JsonProvider` implementation class | `JSONPATH_PROVIDER_CLASS` | _internal class refererence_ |
|`org.ghotibeaun.json.jsonpath.mappingprovider` | Specifies the `com.jayway.jsonpath.spi.mapper.MappingProvider` implementation class | `JSONPATH_MAPPING_PROVIDER_CLASS` | _internal class reference_ |
|`org.ghotibeaun.json.merge.appendkey` | Specifies the text to append to a key with an `APPEND` JSONObject conflict strategy during a merge | `MERGE_APPEND_KEY` | `_append`|
|`org.ghotibeaun.json.parser`          | Specifies the [JSONParser](../javadocs/org/ghotibeaun/json/parser/JSONParser.html) implementation class | `PARSER_CLASS` | _internal class reference_ |
|`org.ghotibeaun.json.serializer`| Specifies the [JSONSerializer](../javadocs/org/ghotibeaun/json/serializer/JSONSerializer.html) implementation class | `SERIALIZER_CLASS` | _internal class reference_ |
|`org.ghotibeaun.json.xmlserializer` | Specifies the [XMLSerializer](../javadocs/org/ghotibeaun/json/serializer/XMLSerializer.html) implementation class | `XML_SERIALIZER_CLASS` | _internal class reference_ |



