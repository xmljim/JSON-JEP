[JEP API Guide](index.html) >> Marshalling and Unmarshalling JSON and POJOs

# Marshalling and Unmarshalling JSON and POJOs

## Contents

* [Overview](#overview)
    
    * [Basic Rules and Assumptions](#basic-rules-and-assumptions)

* [Basic Example](#basic-example)

* [Parsing JSON to a POJO](#parsing-json-to-a-pojo)

* [Converting a POJO to JSON](#converting-a-pojo-to-json)

* [Working with Interfaces and Abstract Classes](#working-with-interfaces-and-abstract-classes)

* [Working with Lists](#working-with-lists)

* [Working with Objects](#working-with-objects)
  
    * [List of Objects](#list-of-objects)
  
    * [Nested Objects](#nested-objects)

* [Annotations](#annotations)

    * [@TargetClass](#targetclass)
  
    * [@JSONIgnore](#jsonignore)
  
    * [@JSONElement](#jsonelement)
  
    * [@JSONValueConverter](#jsonvalueconverter)
  
    * [@ClassValueConverter](#classvalueconverter)
* [ValueConverters](#valueconverters)
  
    * [AbstractValueConverter](#abstractvalueconverter)
  
    * [Example](#example)
    
        * [JSONValueToLocalDateTime](#jsonvaluetolocaldatetime)
    
        * [LocalDateTimeToLong](#localdatetimetolong)
        
        * [Putting It All Together](#putting-it-all-together)
    
    * [Using a ValueConverter with Converters Methods](#using-a-valueconverter-with-converters-methods)

## Overview

JSON lends itself ready for mapping to plain old Java objects (POJOs) that can be used for a variety of use cases:

- configuration files - store configuration data in JSON and load them into Java configuration classes

- data mapping - map JSON data from a JSON source, i.e., a web service to a POJO for data validation and processing

- state persistence: Save and/or load application state to JSON and from Java to a persistence store

The [org.ghotibeaun.json.converters](..javadocs/org/ghotibeaun/json/converters/package-summary.html) contains three core interfaces to 
marshal and unmarshal to and from JSON and POJOs:

- [Converter](..javadocs/org/ghotibeaun/json/converters/Converter.html) - this is the main interface for all converters
- [JSONConverter](javadocs/org/ghotibeaun/json/converters/JSONConverter.html) - contains methods to unmarshal JSON to a POJO class instance
- [ClassConverter](javadocs/org/ghotibeaun/json/converters/ClassConverter.html) - contains methods to marshal a POJO class instance to JSON

In addition to these interfaces, the [Converters](javadocs/org/ghotibeaun/json/converters/Converters.html) class is repsonsible for
instantiating concrete implementations of these interfaces and contains static methods mapped to all of the interface methods.

### Basic Rules and Assumptions

The following are some basic rules and assumptions to follow when marshalling and unmarshalling JSON and POJOs. The are some exceptions,
and we'll discuss these in detail further down. 

- POJOs must be concrete classes. They can extend other classes and implement interfaces. 
- POJOs _must_ have a public default zero-argument constructor. 
- POJOs must have field names that match JSON property names, unless annotated with a `@JSONElement` annotation. 
  For example, if you have property in your JSON data called `name`, there should be a corresponding 
  Java field called `name` with the appropriate type.
- Setter methods must match the following pattern: _`set[FieldName]`_. For the JSON property `name`, the setter method should
  be named `setName`. Converters will first attempt to set/get values from fields on the target class.
- Getter methods must match the following pattern: _`get[FieldName]`_. The one exception is for boolean values, and will also accept _`is[FieldName]`_.
  For the JSON property `name`, the getter method should be named `getName`. Converters will use setter/getter methods on inherited classes.

[\[Back to Top\]](#contents)

## Basic Example

Let's assume a very basic messenger service that transmits data as a JSON instance:

```json
{
    "to": "xml.jim@gmail.com",
    "from": "developer@example.com",
    "message": "JEP is so cool!"
}
```

Now assume that we're consuming that data in our Java application.  We can create a POJO that we can use to store and access that data:

```java
public class Message {
    private String to;
    private String from;
    private String message;

    /**
     * Constructor
     */
    public Message() {

    }
    //create our setters and getters

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

```

With JEP, we can use the [Converters](../javadocs/org/ghotibeaun/json/converters/Converters.html) class to convert our JSON to a new
`Message` class instance, using the `convertToClass(Class<T> targetClass, JSONObject data)` method:

```java
JSONFactory factory = JSONFactory.newFactory();
JSONParser parser = factory.newParser();
JSONObject messageData = parser.parse();

//convert to Message class
Message message = Converters.convertToClass(Message.class, messageData);

//print message
System.out.println(message.getMessage());

```
[\[Back to Top\]](#contents)

## Parsing JSON to a POJO

The [JSONParser](../javadocs/org/ghotibeaun/json/parser/JSONParser.html) interface also includes methods that support
parsing JSON data directly to a POJO:

```java
JSONFactory factory = JSONFactory.newFactory();
JSONParser parser = factory.newParser();

Message message = parser.parse(Paths.get("path/to/json/data"), Message.class);
```
[\[Back to Top\]](#contents)

## Converting a POJO to JSON

With JEP, we can use the [Converters](../javadocs/org/ghotibeaun/json/converters/Converters.html) class to convert our `Message` class
instance to a JSON object, using the `convertToJSONObject(T source)` method:

```java
Message message = new Message();
message.setTo("developer@example.com");
message.setFrom("xml.jim@gmail.com");
message.setMessage("Hey, thanks!");

JSONObject msg = Converters.convertToJSONObject(message);
System.out.println(msg.getString("message"));
```

If I serialize the `msg` using `prettyPrint()`, I'll get this:

```json
{
    "to": "devloper@example.com",
    "from": "xml.jim@gmail.com",
    "message": "Hey, thanks!"
}
```
[\[Back to Top\]](#contents)

## Working with Interfaces and Abstract Classes

It's very common for Java applications to work with interfaces or abstract classes. As long as we
have a concrete implementation class, we can convert a JSON instance. Let's assume we have JSON instance 
containing data about teams, in this particular case, baseball teams. We're working with an interface called `Team`
that could hold teams of any type (basketball, football, hockey).  

We can create an instance of our `Team` interface by telling the `Converter` what our target class will be
(`BaseballTeam.class`).

```json
{
    "name": "Boston Red Sox",
    "city": "Boston",
    "state": "MA",
    "stadium": "Fenway Park",
    "league": "American League"
}
```

```java
// Team.java
public interface Team {
    void setName(String name);

    String getName();

    void setCity(String city);

    String getCity();

    void setState(String state);

    String getState();
}

// BaseballTeam.java
public class BaseballTeam implements Team {
    private String name;
    private String city;
    private String state;
    private String stadium;
    private String league;

    public BaseballTeam() {

    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    String getCity() {
        return city;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String getState() {
        return state;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getStadium() {
        return stadium;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getLeague() {
        return league;
    }


}

//create an instance of our Team interface by telling the converter the concrete class to create
Team team = Converters.convertToClass(BaseballTeam.class, teamData);
System.out.println(team.getName());

//We can still access the data from our BaseballTeam class
System.out.println(((BaseballTeam)team).getLeague());

```

If we round-trip our `Team` instance, it will include all of the data for the underlying class, `BaseballTeam`.

Likewise, if we created an abstract class that implemented the `Team` interface, and refactored the 
`BaseballTeam` class to extend the abstract class, everything will work the same:

```java
// AbstractTeam.java
public abstract class AbstractTeam implements Team {
    private String name;
    private String city;
    private String state;

    public AbstractTeam() {

    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    String getCity() {
        return city;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String getState() {
        return state;
    }
}

//BaseballTeam.java
public class BaseballTeam extends AbstractTeam {
    private String stadium;
    private String league;

    public BaseballTeam() {
        super();
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getStadium() {
        return stadium;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getLeague() {
        return league;
    }
}
```
[\[Back to Top\]](#contents)

## Working with Lists

We already know that JSON isn't just keys with primitive values. They can contain lists as well:

```json
{
    "continent": "Europe",
    "cities": [
        "London",
        "Paris",
        "Barcelona",
        "Berlin",
        "Warsaw",
        "Prague"
    ]
}

```

We can create a POJO class with our `cities` using Java's `List` interface:

```java
public class Places {
    private String continent;
    private List<String> cities;

    public Places() {

    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getContinent() {
        return continent;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public List<String> getCities() {
        return cities;
    }

}
```

Now we convert our JSON instance to our `Places` class instance:

```java
... //load our JSON...

Places places = Converters.convertToClass(Places.class, classData);
System.out.println(places.getCities().get(3)); //Berlin

```
[\[Back to Top\]](#contents)

## Working with Objects

To this point, we've worked with native JSON value types: Strings, Numbers, Booleans and Nulls. 
Even our [List](#working-with-lists) example was a simple list of Strings. We can also convert
more complex values store as `JSONObject`s in our data:

```json
{
    "continent": "Europe",
    "location" : {
        "city": "London",
        "country": "United Kingdom"
    }
}
```

Our POJO might look like this:

```java
// Place.java

public class Place {
    private String continent;
    private Location location;

    public Place() {

    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getContinent() {
        return continent;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}

// Location.java
public class Location {
    private String city;
    private String country;

    public Location() {

    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }
}

```

Following our [basic rules](#basic-rules-and-assumptions), as long as the underlying type is a public concrete class with a 
default, zero-argument constructor, the `JSONConverter` will interpolate the field type and correctly instantiate the 
`Location` class for the `location` property.

```java
... //load our JSON...

Place place = Converters.convertToClass(Place.class, classData);
System.out.println(place.getLocation().getCity()); //London

```

### List of Objects

What if our JSON data includes a list of objects? Converter's will interrogate the `List`'s generic type parameter
to determine which class to instantiate. Again, following our [basic rules](#basic-rules-and-assumptions), as long as
the generic parameter type is a public concrete class with a default, zero-argument constructor, the `JSONConverter` 
will know how to initialize the object.

Let's assume our JSON data organizes our locations as a list of `Place`s:

```json
{
    "globalCities": [
        {
            "continent": "Europe",
            "location" : {
                "city": "London",
                "country": "United Kingdom"
            }
        }
    ]
}
```

Our POJO would look something like this (we'll borrow our existing [Place](#working-with-objects) class we created previously):

```java
public class Atlas {
    private List<Place> globalCities;

    public Atlas() {

    }

    public void setGlobalCities(List<Place> globalCities) {
        this.globalCities = globalCities;
    }

    public List<Place> getGlobalCities() {
        return globalCities;
    }

}

```

### Nested Objects

Let's reorganize the JSON again. This time, we'll organize by `continent`, with each property representing a specific content
containing a list of cities (`Location`).

```json
{
    "globalCities" : {
        "europe": [
            {
                "city": "London",
                "country": "United Kingdom"
            },
            {
                "city": "Paris",
                "country": "France"
            }
        ],
        "northAmerica": [
            {
                "city": "New York",
                "country": "United States"
            },
            {
                "city": "Toronto",
                "country": "Canada"
            }
        ],
        "asia": [
            {
                "city": "Shanghai",
                "country": "China"
            },
            {
                "city": "Tokyo",
                "country": "Japan"
            }
        ],
        "africa": [
            {
                "city": "Cairo",
                "country": "Egypt"
            },
            {
                "city": "Johannesburg",
                "country": "South Africa"
            }
        ],
        "australia": [
            {
                "city": "Sydney",
                "country": "Australia"
            },
            {
                "city": "Melbourne",
                "country": "Australia"
            }
        ],
        "southAmerica": [
            {
                "city": "Sao Paulo",
                "country": "Brazil"
            },
            {
                "city": "Buenos Aires",
                "country": "Argentina"
            }
        ]
    }
}
```

We would structure our POJOs like this:

```java
// Atlas.java 
public class Atlas {
    private Continents globalCities;

    //getters and setters...
}

// Continents.java
public class Continents {
    private List<Location> northAmerica;
    private List<Location> europe;
    private List<Location> asia;
    private List<Location> africa;
    private List<Location> australia;
    private List<Location> southAmerica;

    //getters and setters...
    
}
```
[\[Back to Top\]](#contents)

## Annotations

There are cases where the basic rules don't apply.  For example, it's very common to have a fields that are 
intrinsically typed to an interface (or a generic of an interface). There are other cases where JSON data types
do not map intrinsically to Java object types. Another perfect example is date objects. Natively, JSON does not 
have a data type for dates, so developers will typically use either a Unix-style timestamp (`Long`) or a
date/date-time string mapped to the ISO-8601 date format.  

Another example might be that POJO and JSON data have different field-to-property mappings, or we want to 
use a different setter or getter method. Perhaps we simply want to ignore some JSON properties or POJO fields from
mapping. All of these scenarios can be accomplished with annotation interfaces that can be applied to different 
parts of your POJO.

### @TargetClass

The [`@TargetClass`](javadocs/org/ghotibeaun/json/converters/annotation/TargetClass.html) annotation allows you 
to specify a concrete class to use for a given JSON property containing either a List of objects, or another JSONObject. 
This is particularly useful if you wish to use a particular concrete class implementation that overrides the designated
field type, or if the field type is an interface or abstract class.  

There is a distinction between assigning a target class parameter on the `convertToClass` method and the `@TargetClass` annotation:
The target class parameter applies to the _entire_ POJO; the `@TargetClass` annotation applies to the specific field or method. So
it's quite possible to have your POJO using a different class than a field within your POJO.

> **NOTE**: You do not need to use a `@TargetClass` annotation for native JSON types (Strings, Boolean, Numbers, Null), which will
> throw an exception if you do. You also do not need to use the `@TargetType` annotation if your field type is a simple POJO 
> that follows the [basic rules](#basic-rules-and-assumptions)

The `@TargetClass` annotation requires that you include a valid `Class<?>` reference. In the example below, we'll annotate 
a List of `Team`s, where `Team` is an interface:

```java
// snip
@TargetClass(BaseballTeam.class)
private List<Team> teams;
// snip
```

This would map to a JSON structure similar to this:

```json
{
    "teams": [
        {
            "name": "Boston Red Sox",
            "stadium": "Fenway Park",
            "league": "American League",
            "worldSeriesWins": [
                1903,
                1912,
                1915,
                1916,
                1918,
                2004,
                2007,
                2013,
                2018
            ]
        },
        {
            ...
        }
    ]
}
```

This will instruct the JSONConverter to create a `BaseballTeam` instance for each item in the JSONArray backed by the property `teams`.
Similarly, you can apply the `@TargetClass` annotation to a JSONObject:

```java
//snip
@TargetClass(BaseballTeam.class)
private Team team;
//snip
```

### @JSONIgnore

There may be cases where you do not wish to map a POJO field to a JSON property. In this case you can use the 
[`@JSONIgnore`](javadocs/org/ghotibeaun/json/converters/annotation/JSONIgnore.html) annotation.

```java
@JSONIgnore
private String teamName;
```

By default the native `value()` is set to `true`.  If you want to change the default behavior, then you can
set the `value()` to `false`:

```java
@JSONIgnore(false)
private String teamName;
```

### @JSONElement

The [`JSONElement`](javadocs/org/ghotibeaun/json/converters/annotation/JSONElement.html) annotation is used for
mapping JSON properties to a specific property name and/or to a specific setter or getter method in your POJO.
There are three optional fields:

- `key()`: specifies the specific JSON property key a field is mapped to to, useful when the field and property names do not match
- `setterMethod()`: specifies a setter method to use when setting a JSON value. This can be useful for handling
  data type issues or for other specific needs where a JSON property value will be processed further. Only needed when you 
  want to convert a JSONObject to a POJO.
- `getterMethod()`: specifies a getter method to use when retrieving a POJO field value to set to a JSONObject instance. This can
  be useful for converting a complex value into a data type that is easily consumed by JSON. Only needed if you're 
  planning to convert a POJO to a JSONObject instance

You do not need to assign a `setterMethod()` or `getterMethod()` with a `key()`. For example, you could set a `key()` value
to map a field to particular JSON property name, yet the field name and the setter and getter method names are internally
consistent. Likewise, you could set the `setterMethod()` and `getterMethod()` properties without setting the `key()` value
if the field and property names match. 

```java
//example 1 - map a key: maps the 'teams' field to the 'teamArray' property in the JSON data
@JSONElement(key = "teamArray")
private List<Team> teams;
...
public void setTeams(List<Team> team) {...}
public List<Team> getTeams() {...}

//example 2 - map a setter and getter (field and property names match)
@JSONElement(setterMethod = "setTeamList", getterMethod = "getTeamList")
private List<Team> teamArray;

public void setTeamList(List<Team> teams) {...}
public List<Team> getTeamList() {...}

//example 3 - map all three properties
@JSONElement(key = "teamArray", setterMethod = "setTeamList", getterMethod = "getTeamList")
private List<Team> teams;

public void setTeamList(List<Team> teams) {...}
public List<Team> getTeamList() {...}
```

### @JSONValueConverter
The [`@JSONValueConverter`](javadocs/org/ghotibeaun/json/converters/annotation/JSONValueConverter.html) annotation can used 
on a field or method that defines a [`ValueConverter`](#valueconverters) to be applied to a JSON property value before it is
set to a POJO field. **This is only applied when unmarshalling a JSON instance to a POJO**. There is a sibling annotation that
is used for converting a Class value into a JSON value: [`@ClassValueConverter](#classvalueconverter)

> **IMPORTANT**: Do _not_ apply `@JSONValueConverter` or `@ClassValueConverter` along with a [`@TargetClass`](#targetclass) annotation.
> An exception will be thrown if you.

In one way, the `JSONValueConverter` behaves in the same way that `setterMethod()` does for a [`@JSONElement`](#jsonelement) would if you
implemented a conversion there. However, by using the `JSONValueConverter`, and specifying a particlular class, you 
get the benefit of reuse. 

See [ValueConverters](#valueconverters) for more information about implementing them for use by this annotation

The `JSONValueConverter` contains two required fields:

- `converter()`: This is a `Class` that must inherit from the `ValueConverter` interface
- `args()`: This is `String[]` array of any arguments that may be used for this converter. You can specify an empty array `{}` if 
  no arguments are needed. 

For this example, let's assume we have a JSON instance that includes a `phone` String, and a `dateTime` property that stores
a phone call's date and time they called as a time stamp.  We want to handle the phone a as PhoneNumber class that 
breaks the phone String into an area code, prefix, and number.  For the `dateTime` time stamp, we want to convert it
to a `LocalDateTime` value.

For our `LocalDateTime` converter, we need to specify the format to use to parse the JSON value. Since we're using a 
`Long` timestamp format, we'll use `timestamp` as our format argument


```java
@JSONValueConverter(converter = PhoneNumberConverter.class, args = {})
private PhoneNumber phone;

@JSONValueConverter(converter = LocalDateTimeConverter.class, args = {"timestamp"})
private LocalDateTime dateTime;

```

### @ClassValueConverter

The `@ClassValueConverter` is the sibling annotation for implementing a [ValueConverter](#valueconverters) that is
used when marshalling a POJO into a JSON instance. It includes the same fields as the [`@JSONValueConverter`](#jsonvalueconverter).
They are distinct annotations so that you have more control over marshalling and unmarshalling behavior.

[\[Back to Top\]](#contents)

## ValueConverters

<a name="ref-footnote-1"></a>ValueConverters are designed to convert values from native JSON primitive types to more complex Java types, and vice-versa. For example, Java includes a rich library of classes for handling temporal data, especially with the [`java.time`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/package-summary.html) package; JSON, on the other hand, is limited in its capability to store these values. JSON dates and times are typically expressed in two different ways<sup>[1](#footnote-1)</sup>:

- As a formatted string, i.e., an ISO-8601 date/time string (e.g., `"2020-01-01"` or `"2020-01-01T16:24:34.567"`)
- As a long, typically in the form of a Unix-like time stamp (e.g., `1577921074567`)

All ValueConverters implement the [`ValueConverter`](../javadocs/org/ghotibeaun/json/converters/valueconverter/ValueConverter.html) interface. Implementations are applied to the [JSONValueConverter](#jsonvalueconverter) or [ClassValueConverter](#classvalueconverter) annotation, depending on
the intent of the conversion. ValueConverters are _single purpose_ or _unidirectional_, meaning that they are only applied to values on a JSON instance OR a POJO. 

### AbstractValueConverter

The [`AbstractValueConverter`](../javadocs/org/ghotibeaun/json/converters/valueconverter/AbstractValueConverter.html) implements the `ValueConverter`
interface, and also includes convenience methods to ensure that the value's type is supported, and for accessing any arguments that passed from the 
[JSONValueConverter](#jsonvalueconverter) or [ClassValueConverter](#classvalueconverter) annotation. Concrete classes need to only implement the 
[`getConvertedValue(V)`](../javadocs/org/ghotibeaun/json/converters/valueconverter/AbstractValueConverter.html#getConvertedValue(V)) method and 
return the intended value type.


### Example

In this example, we will create two ValueConverters, one that will convert a JSON timestamp into a `LocalDateTime` instance, and second
ValueConverter that will convert a `LocalDateTime` instance to a long timestamp value for use in a JSON instance. Each class will extend from
`AbstractValueConverter` so that we can take advantage of value checking and easy access to any arguments passed to our ValueConverter.

#### JSONValueToLocalDateTime

This ValueConverter will be used to convert a long value to a LocalDateTime instance. 

```java
public class JSONValueToLocalDateTime extends AbstractValueConverter<LocalDateTime> {

    /**
     * Constructor
     * @param args any args passed to the Converter
     */
    public JSONValueToLocalDateTime(String... args) {
        super(args);
    }

    /**
     * Returns the class types that this ValueConverter can process. The AbstractValueConverter
     * will evaluate whether the incoming value is one of these types
     */
    @Override
    public Class<T>[] accepts() {
        return new Class[] {Long.class, long.class};
    }

    /**
     * Return the converted value. This method is called after the AbstractValueConverter checks that the
     * incoming value type is supported.
     */
    @Override
    public <V> LocalDateTime getConvertedValue(V value) throws JSONConversionException {
        final LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli((Long)value), ZoneId.systemDefault());
        return ldt;
    }
}
```

#### LocalDateTimeToLong

```java
public class LocalDateTimeToLong extends AbstractValueConverter<Long> {
    
    /**
     * Constructor
     * @param args any args passed to this ValueConverter
     */
    public LocalDateTimeToLong(String... args) {
        super(args);
    }

    /**
     * Returns the class types that this ValueConverter can process. The AbstractValueConverter
     * will evaluate whether the incoming value is one of these types
     */
    @Override
    public Class<T>[] accepts() {
        return new Class[] {LocalDateTime.class};
    }

    /**
     * Return the converted value. This method is called after the AbstractValueConverter checks that the
     * incoming value type is supported.
     */
    @Override
    public <V> Long getConvertedValue(V value) throws JSONConversionException {
        final ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset((LocalDateTime)value);
        return ((LocalDateTime)value).toInstant(offset).toEpochMilli();
    }       
}
```

#### Putting It All Together

Now that we've created our ValueConverters, we can apply them to our POJO using the [JSONValueConverter](#jsonvalueconverter)
and [ClassValueConverter](#classvalueconverter) annotations on our POJO class variable:

```java
//snip
@JSONValueConverter(converter = JSONValueToLocalDateTime.class, args = {}) //convert a JSON long value to a LocalDateTime
@ClassValueConverter(converter = LocalDateTimeToLong.class, args = {}) //convert a LocalDateTime to a long (timestamp)
private LocalDateTime dateTime;
//snip
```

### Using a ValueConverter with Converters Methods

We've already discuss the [Converters](..javadocs/org/ghotibeaun/json/converters/Converters.html) class, which provides static methods
to marshal and unmarshal JSON and POJOs. Another common scenario for applying a ValueConverter is converting lists of values to JSONArrays,
and vice versa. Let's start with an example that is a JSONArray of long values that represent time stamps. We want to convert it to a 
`List<LocalDateTime>`. 

```json
[
    1613575337508,
    1611212400000
]
```
Now let's convert it to the Java List instance:

```java
JSONArray listOfDateTimes = JSONFactory.newFactory().newParser().parse(...);

ValueConverter<LocalDateTime> timestampConverter = new JSONValueToLocalDateTime();
List<LocalDateTime> list = Converters.convertToList(listOfDateTimes, Optional.of(timestampConverter), Optional.empty());
```

In the example above, we instantiated our ValueConverter that converts timestamp (long) values to LocalDateTime instances. Then
we called the `convertToList(JSONArray, Optional<ValueConverter<?>>, Optional<Class<?>>)` method. The `Optional<?>` class allows for
either an instance of the designated type, or null.  Since we're passing in our ValueConverter, we use the `Optional.of()` method to 
assign the the ValueConverter.  Likewise, since we're not using a Target Class, we assign a value of `Optional.empty()`. 

This can also be used in reverse. Let's take our `List<LocalDateTime>` instance and convert it to a list of timestamps:

```java
ValueConverter<Long> dateTimeConverter = new LocalDateTimeToLong();
JSONArray timestampArray = Converters.convertToJSONArray(list, Optional.of(dateTimeConverter), Optional.empty());
```


[\[Back to Top\]](#contents)

---
<a name="footnote-1">[\[1\]](#ref-footnote-1) Of course, there _are_ other ways to express date/time values in JSON, but these are the most common
</a>