[JEP API Guide](./index) >> [Getting Started](./getting-started) >> Creating an Event Handler

# Creating an Event Handler

## Contents

* [Overview](#overview)
* [Setting Up The Class](#setting-up-the-class)
    * [Understanding The JSON Event Model](#understanding-the-json-event-model)
    * [documentStart](#documentstart)
    * [documentEnd](#documentend)
    * [newKey](#newkey)
    * [jsonArrayStart](#jsonarraystart)
    * [jsonArrayEnd](#jsonarrayend)
    * [jsonObjectStart](#jsonobjectstart)
    * [value* Events](#value-events)
* [Putting It All Together](#putting-it-all-together)
* [Using Our Event Handler](#using-our-event-handler)
* [Some Additional Thoughts and Conclusion](#some-additional-thoughts-and-conclusion)

## Overview
Event Handlers are an effective way of extracting, filtering and even transforming JSON data. It could be that the data source is massive
and you only care about a slice of it; it could be that you only care about slices of data `within` the data objects themselves. Whatever the 
case, you can implement an Event Handler to address specific needs.

All Event Handlers must implement the [`JSONEventHandler`](../javadocs/org/ghotibeaun/json/parser/jep/eventhandler/JSONEventHandler.html)  interface. Generally speaking,
this is a low-level interface and should not be implemented directly. Instead, it's recommended to extend the 
[EventHandler](../javadocs/org/ghotibeaun/json/parser/jep/eventhandler/EventHandler.html) abstract class, or one of the built-in abstract class that
inherit from it. 

In this example, we'll use a scenario of a massive JSON file that stores aggregated time-series data, updated daily, containing information about COVID-19 cases
nationally. Each record contains a date field (stored as a ISO-8601 date-time string) for the recorded date, a state and county field.  We're only interested in cases in Boulder County, Colorado for the last month.  The feed is available from a URL. The data structure of the feed looks like the following:

```json
{
    "feed": {
        "lastUpdated": "2021-02-22T16:38:42Z"
    },
    "data": [
        {
            "reportDate": "2021-02-20T16:38:42Z",  //ISO-8601 date-time string, e.g., "2021-02-20T16:38:42Z"
            "state": "CO",
            "county": "Boulder",
            "gender": "M",
            "age": 63,
            "race": "WNH", //White Not Hispanic
            "occupationCode": { //Dept of Labor SOC labor codes
                "group": 49,
                "code": 3041
            },
            "maritalStatus": "D",
            "homeOwnership": "rent",
            "homeZipCode": "80399",
            "numberOfOccupants": 2,
            "dateFirstSymptoms": "2021-02-16",
            "hospital": false
        }
        ...
    ]
}
```

## Setting up the Class
We'll create our class to extend the [StackEventHandler](../javadocs/org/ghotibeaun/json/parser/jep/eventhandler/StackEventHandler.html) abstract class.
This class does a lot of the heavy lifting of processing the data coming from the `JSONEventProvider` and `JSONEventProcessor` to fire the appropriate
entity event. It also includes some nice little features that allow you to interrogate where you are in context of both keys and object/array types. 

> **NOTE**: The `StackEventHandler` class does not have any method or variable to hold our collected data, we need to include on in our class. 
> For our purposes, we're collecting data as a list, so we'll implement ours as a `JSONArray`. We _could_ initialize our data on the
> [documentStart](../javadocs/org/ghotibeaun/json/parser/jep/eventhandler/StackEventHandler.html#documentStart(org.ghotibeaun.json.JSONValueType)) method, but since 
> we're aggregating a known set, it's just as easy to initialize right away.  We'll also need a public method to return the data.

We'll set up the class with three parameters

- State
- County (Can be null if we want to aggregate by state instead)
- Date represented as a [LocalDateTime](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/LocalDateTime.html)

```java

public class CovidEventHandler extends StackEventHandler {
    private final String stateCriteria; //our state criteria value
    private final String countyCriteria; //our county criteria - can be null if we want to aggregate by the entire state
    private final LocalDateTime dateCriteria;  //our date criteria

    //Initialize our JSONArray that will hold our filtered records
    private final JSONArray requestedData = NodeFactory.newJSONArray();

    /**
     * Constructor
     * @param stateCriteria the state we want to filter data
     * @param countyCriteria the county we want filter. Can be null to accept all counties
     * @param dateCriteria the date criteria. Accept only records on or after this date.
     */
    public CovidEventHandler(String stateCriteria, String countyCriteria, LocalDateTime dateCriteria) {
        super();
        this.stateCriteria = stateCriteria;
        this.countyCriteria = countyCriteria;
        this.dateCriteria = dateCriteria;
    }

    //public getter to return our data after we're finished
    public JSONArray getRecords() {
        return requestedData;
    }

    //set up private methods to get and evaluate our data. Not required, but good practice

    /**
     * Return the state criteria
     * @return The state criteria
     */
    private String getStateCriteria() {
        return stateCriteria;
    }

    /**
     * Return the county criteria
     * @return The county criteria, could be null
     */
    private String getCountyCriteria() {
        return countyCriteria;
    }

    /**
     * Return the date criteria
     * @return The date criteria
     */
    private LocalDateTime getDateCriteria() {
        return dateCriteria;
    }

    /**
     * Evaluate a state value against our criteria
     * @param state the state value from the current record
     * @return true if the state matches; false otherwise
     */
    private boolean meetsStateCriteria(String state) {
        return getStateCriteria().equals(state);
    }

    /**
     * Evaluate a county value against our criteria
     * @param county the county value from the current record
     * @return if #getCountyCriteria() returns null, then it returns true. Otherwise if the county matches, it returns true, and false otherwise
     */
    private boolean meetsCountyCriteria(String county) {
        if (getCountyCriteria() == null) {
            return true;
        } else {
            return getCountyCriteria().equals(county);
        }
    }

    /**
     * Parse and evaluate a date value against our criteria
     * @param date the dateTime string from our record
     * @return true if the date is on or after our date criteria; false otherwise
     */
    private boolean meetsDateCriteria(String date) {
        final LocalDateTime ldt = LocalDateTime.parse(date, DateTimeFormatter.ISO_INSTANT);

        return ldt.isEqual(getDateCriteria()) || ldt.isAfter(getDateCriteria());
    }
}

```
### Understanding the JSON Event Model

Now that we've set up the class we need to implement its abstract methods inherited from the `JSONEventHandler` interface.  These methods are fired by a 
[JSONEventProvider](../javadocs/org/ghotibeaun/json/parser/jep/eventprovider/JSONEventProvider.html) and initially processed by the `EventHandler`, and then
subsequently fired to the appropriate event method. 

Now that we've set up the class and constructor, we need to implement the `JSONEventHandler` event methods:

- `documentStart(JSONValueType)`: fired when a JSON document is started and the start container (either '{' or '['). The `JSONValueType` returns the type of container (either `OBJECT`, or `ARRAY`).
- `documentEnd()`: fired when the last container marker is read and there is no further data.
- `newKey(String)`: Fired when a new JSONObject key is found
- `jsonArrayStart(String)`: fired at the start of a new JSONArray. It also passes the key of the parent or _ancestor_ JSONObject if it exists, or `null` if one doesn't exist
- `jsonArrayEnd(String)`: fired when a JSONArray boundary ends.
- `jsonObjectStart(String)`: fired at the start of a new JSONObject, also passing in the key of the parent or _ancestor_ JSONObject if it exists, or `null` if one doesn't exist
- `jsonObjectEnd(String)`: fired at the close a JSONObject. It's up to each implementing class to keep track of the Object stack.
- `valueBigDecimal(String, BigDecimal)`: fired when a `BigDecimal` value is encountered.
- `valueBoolean(String, boolean)`: fired when a boolean value is encountered.
- `valueDouble(String, double)`: fired when a double value is encountered.
- `valueFloat(String, float)`: fired when a float value is encountered.
- `valueInt(String, int)`: fired when an integer value is encountered.
- `valueLong(String, long)`: fired when a long value is encountered.
- `valueNull(String)`: fired when a null value is encountered.
- `valueString(String, String)`: fired when a String value is encountered.

### documentStart

We know the data is regular given our structure above, so we really don't care much about this event:

```java
@Override
public void documentStart(JSONValueType type) {
   //nothing to do here
}
```

### documentEnd 

Since we're collecting data on the fly and appending the records to our `data` variable, there's nothing to do

```java
@Override
public void documentEnd() {
    //nothing to do here
}
```

### newKey

`newKey` fires at the start of a new element, we can handle our data through different event methods, so we'll 
implement it as a no-op

```java
@Override
public void newKey(String key) {
    //nothing to do here
}
```

### jsonArrayStart

We do care about this event since all of the data we care about is in a JSONArray with the key "`data`."  We want to create a flag
for our value events to tell them whether or not to handle the value. We'll need a new field in our class that will be used as a flag 
and to implement the value methods.

```java
//flag to indicate if we're in the data array of our source data
private boolean inData = false;

...

@Override
public void jsonArrayEnd(String key) {
    inData = key.equals("data");  //turn the inData flag on if the current key is "data"
}

```

### jsonArrayEnd
In our case, there's only one array, the "data" array.  Defensively, let's turn our `inData` flag off:

```java
@Override
public void jsonArrayEnd(String key) {
    inData = !key.equals("data");
}
```

### jsonObjectStart

There are some data that are structured as JSON objects, so we need to pay attention to these. Using the `key` parameter, we can determine
the parent or ancestor JSONObject that holds this object.  The `StackEventArray` class has methods that allow use to interrogate the 
context of our keys, and the `JSONValueType` of the objects in our traversal stack, but it intentionally does not maintain a
_node stack_ of which Object we're traversing through.  That's where the `jsonObjectStart` and `jsonObjectEnd` events help us to determine
how we want to address the data coming at us through these events.  

> It's important to note here what I mean by _ancestor_: If a object's or value's direct parent is a JSONArray, then there is no key tied to it
> that informs us of the context, so we can go up the stack to find the nearest JSONObject holding the array. This is important in this example
> since each record is contained in a JSONArray property named '`data`'.  We can use the key to trigger whether or not we want to instantiate
> a new `JSONObject` instance. It's not expensive to create one, but it's just more junk for the Garbage Collector to handle, so less is 
> more in this case. 

What we need is a LIFO stack. This stack will allow us to push new `JSONObject` instances to it each time the `jsonObjectStart` event is 
fired. Java has a ready-made solution for this: [ArrayDeque<T>](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ArrayDeque.html). We'll create a new field to hold our stack, and then implement the `jsonObjectStart` event. Remember that we only care about data within the "`data`" array, 
so we'll only do anything if our `inData` field is set to `true`.

```java
//create a LIFO stack that we can use for our record, which does contain a nested JSONObject
private final Deque<JSONObject> nodeStack = new ArrayDeque<>();

...
@Override
public void jsonObjectStart(String key) {
    if (inData) {
        nodeStack.push(NodeFactory.newJSONObject());
    }
}

```

### jsonObjectEnd

Our `jsonObjectStart` event method sets up our record stack, so now we can handle the event of the end of a `JSONObject`. 
There are two conditions we care about in our scenario

- The end of the `occupationCode` nested object in our record

- The end of the record itself

This is where our `nodeStack` class field comes into play. We still only care about the event if our `inData` flag is `true`,
but now we also care about the `key` parameter to tell us what to do.

```java
@Override
public void jsonObjectEnd(String key) {
    if (inData) {
        //If the current key is 'occupationCode' then pop and set it on our record
        //important to pop the JSONObject so that the current JSONObject is the record itself
        if (key.equals("occupationCode")) {
            //pop the stack, and hold it
            final JSONObject oc = nodeStack.pop();
            //now add the popped occupation code to the top of stack
            //which is the record object
            nodeStack.getLast().put(key, oc);
        } else {  
            // has to be the record since our stack is pretty shallow.  
            // We could be a little more defensive and make the key value is 'data', but the data is regular

            //pop the record from our stack
            final JSONObject record = nodeStack.pop();
            final String state = record.getString("state");
            final String county = record.getString("county");
            final String reportDate = record.getString("reportDate");

            //if it meets all criteria, then add it to our array, otherwise do nothing
            if (meetsStateCriteria(state) && meetsCountyCriteria(county) && meetsDateCriteria(reportDate)) {
                requestedData.add(record);
            } 
        }
    }
}

```

### value* Events

Now that we have our structure in place, we'll handle the value events. Since we handle context in our 
`nodeStack`, and since it's pretty simple, our value event handlers really only care if the 
`inData` flag is `true`, then we just get the last item in the stack and set the value using the 
`key` parameter.

```java

@Override
public void valueString(String key, String value) {
    if (inData) {
        nodeStack.getLast().put(key, value);
    }
}

@Override
public void valueLong(String key, Long value) {
    if (inData) {
        nodeStack.getLast().put(key, value);
    }
}

@Override
public void valueInt(String key, Integer value) {
    if (inData) {
        nodeStack.getLast().put(key, value);
    }
}

@Override
public void valueBigDecimal(String key, BigDecimal value) {
    if (inData) {
        nodeStack.getLast().put(key, value);
    }
}

@Override
public void valueDouble(String key, Double value) {
    if (inData) {
        nodeStack.getLast().put(key, value);
    }
}

@Override
public void valueFloat(String key, Float value) {
    if (inData) {
        nodeStack.getLast().put(key, value);
    }
}

@Override
public void valueBoolean(String key, boolean value) {
    if (inData) {
        nodeStack.getLast().put(key, value);
    }
}

@Override
public void valueNull(String key) {
    if (inData) {
        nodeStack.getLast().putNull(key);
    }
}
```

## Putting it All Together

Let's take a look at our finished class:

```java
public class CovidEventHandler extends StackEventHandler {
    private final String stateCriteria; //our state criteria value
    private final String countyCriteria; //our county criteria - can be null if we want to aggregate by the entire state
    private final LocalDateTime dateCriteria;  //our date criteria
    
    //Initialize our JSONArray that will hold our filtered records
    private final JSONArray requestedData = NodeFactory.newJSONArray();
    
    //flag to indicate if we're in the data array of our source data
    private boolean inData = false;
    
    //create a LIFO stack that we can use for our record, which does contain a nested JSONObject
    private final Deque<JSONObject> nodeStack = new ArrayDeque<>();

    /**
     * Constructor
     * @param stateCriteria the state we want to filter data
     * @param countyCriteria the county we want filter. Can be null to accept all counties
     * @param dateCriteria the date criteria. Accept only records on or after this date.
     */
    public CovidEventHandler(String stateCriteria, String countyCriteria, LocalDateTime dateCriteria) {
        super();
        this.stateCriteria = stateCriteria;
        this.countyCriteria = countyCriteria;
        this.dateCriteria = dateCriteria;
    }

    //public getter to return our data after we're finished
    public JSONArray getRecords() {
        return requestedData;
    }

    //set up private methods to get and evaluate our data. Not required, but good practice

    /**
     * Return the state criteria
     * @return The state criteria
     */
    private String getStateCriteria() {
        return stateCriteria;
    }

    /**
     * Return the county criteria
     * @return The county criteria, could be null
     */
    private String getCountyCriteria() {
        return countyCriteria;
    }

    /**
     * Return the date criteria
     * @return The date criteria
     */
    private LocalDateTime getDateCriteria() {
        return dateCriteria;
    }

    /**
     * Evaluate a state value against our criteria
     * @param state the state value from the current record
     * @return true if the state matches; false otherwise
     */
    private boolean meetsStateCriteria(String state) {
        return getStateCriteria().equals(state);
    }

    /**
     * Evaluate a county value against our criteria
     * @param county the county value from the current record
     * @return if #getCountyCriteria() returns null, then it returns true. Otherwise if the county matches, it returns true, and false otherwise
     */
    private boolean meetsCountyCriteria(String county) {
        if (getCountyCriteria() == null) {
            return true;
        } else {
            return getCountyCriteria().equals(county);
        }
    }

    /**
     * Parse and evaluate a date value against our criteria
     * @param date the dateTime string from our record
     * @return true if the date is on or after our date criteria; false otherwise
     */
    private boolean meetsDateCriteria(String date) {
        final LocalDateTime ldt = LocalDateTime.parse(date, DateTimeFormatter.ISO_INSTANT);

        return ldt.isEqual(getDateCriteria()) || ldt.isAfter(getDateCriteria());
    }

    @Override
    public void documentStart(JSONValueType type) {
        //nothing to do here
    }

    @Override
    public void documentEnd() {
        //nothing to do here
    }

    @Override
    public void jsonArrayStart(String key) {
        inData = key.equals("data");
    }

    @Override
    public void jsonArrayEnd(String key) {
        inData = !key.equals("data");
    }

    @Override
    public void jsonObjectStart(String key) {
        if (inData) {
            nodeStack.push(NodeFactory.newJSONObject());
        }
    }

    @Override
    public void jsonObjectEnd(String key) {
        if (inData) {
            if (key.equals("occupationCode")) {
                //pop the stack, and hold it
                final JSONObject oc = nodeStack.pop();
                //now add the popped occupation code to the top of stack
                //which is the record object
                nodeStack.getLast().put(key, oc);
            } else {
                // has to be the record since our stack is pretty shallow.  
                // We could be a little more defensive and make the key value is 'data', but the data is regular

                //pop the record from our stack
                final JSONObject record = nodeStack.pop();
                final String state = record.getString("state");
                final String county = record.getString("county");
                final String reportDate = record.getString("reportDate");

                //if it meets all criteria, then add it to our array, otherwise do nothing
                if (meetsStateCriteria(state) && meetsCountyCriteria(county) && meetsDateCriteria(reportDate)) {
                    requestedData.add(record);
                } 
            }
        }
    }

    @Override
    public void valueString(String key, String value) {
        if (inData) {
            nodeStack.getLast().put(key, value);
        }
    }

    @Override
    public void valueLong(String key, Long value) {
        if (inData) {
            nodeStack.getLast().put(key, value);
        }
    }

    @Override
    public void valueInt(String key, Integer value) {
        if (inData) {
            nodeStack.getLast().put(key, value);
        }
    }

    @Override
    public void valueBigDecimal(String key, BigDecimal value) {
        if (inData) {
            nodeStack.getLast().put(key, value);
        }
    }

    @Override
    public void valueDouble(String key, Double value) {
        if (inData) {
            nodeStack.getLast().put(key, value);
        }
    }

    @Override
    public void valueFloat(String key, Float value) {
        if (inData) {
            nodeStack.getLast().put(key, value);
        }
    }

    @Override
    public void valueBoolean(String key, boolean value) {
        if (inData) {
            nodeStack.getLast().put(key, value);
        }
    }

    @Override
    public void valueNull(String key) {
        if (inData) {
            nodeStack.getLast().putNull(key);
        }
    }

    @Override
    public void newKey(String key) {
        //nothing to do here. We're handling keys from the jsonArrayStart, jsonArrayEnd and value methods as needed
    }

}

```

## Using Our Event Handler

Now that we've created our EventHandler, let's use it:

```java
// set up our criteria
private String state = "CO";
private String county = "Boulder";
private LocalDateTime date = LocalDateTime.parse("2021-02-20T00:00:00Z");

//initialize our handler with our criteria
CovidEventHandler eventHandler = new CovidEventHandler(state, county date);

// Create a new ParserSettings class this will create a configuration of your handler, along with
// the default JSONEventProcessor and default JSONEventProvider
ParserSettings settings = ParserSettings.newConfiguration(eventHandler);

//Create a new EventParser
JSONEventParser parser = EventParser.newEventParser();
//Start the process using an inputstream, file, and the ParserSettings instance
parser.parse(theInputStream, settings);

//Now we can use the data:

JSONArray records = eventHandler.getRecords();
```

## Some Additional Thoughts and Conclusion

This scenario makes the assumption that you want to read the filtered records into memory as a `JSONArray` of `JSONObject`s.  But what if
you have a requirement to push the filtered records to a NoSQL database like OrientDB? In this scenario we could modify the `jsonObjectEnd`
method to create a connection to our database and insert the record rather than a simple `add(JSONObject)`.  

Event Handlers give you the flexibility to work with JSON data _as it's structured_, and without the overhead of loading all of the data
into memory before you can work with it, which is a huge performance gain. It also supports cases where you want to stream data from one
location to another, and potentially transforming it on the fly, for a myriad of use cases including data analytics, building data lakes,
or for ETL to other systems. Whatever the use case, it's possible to build an Event Handler to address it. 