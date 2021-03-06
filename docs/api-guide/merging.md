# Merging JSON Data

## Contents

* [Overview](#overview)
* [Basic Merging Scenarios](#basic-merging-scenarios)
* [A Few More Complex Examples](#a-few-more-complex-examples)
* [Managing Conflicts](#managing-conflicts)
* [The `ConflictStrategy` Interface](#the-conflictstrategy-interface)
    * [JSONObject Conflict Strategies](#jsonobject-conflict-strategies)
        * [`AcceptPrimaryConflictStrategy`](#acceptprimaryconflictstrategy)
        * [`AcceptSecondaryConflictStrategy`](#acceptsecondaryconflictstrategy)
        * [`AppendObjectConflictStrategy`](#appendobjectconflictstrategy)
    * [JSONArray Conflict Strategies](#jsonarray-conflict-strategies)
        * [`AppendArrayConflictStrategy`](#appendarrayconflictstrategy)
        * [`InsertBeforeConflictStrategy`](#insertbeforeconflictstrategy)
        * [`InsertAfterConflictStrategy`](#insertafterconflictstrategy)
        * [`DeduplicateArrayConflictStrategy`](#deduplicatearrayconflictstrategy)

## Overview

Merging functionality is supported from the [`org.ghotibeaun.json.merge`](../javadocs/org/ghotibeaun/json/merge/package-summary.html) package.
The [`MergeProcess`](../javadocs/org/ghotibeaun/json/merge/MergeProcess.html) interface that orchestrates the merging of JSONNode instances, and defines a single method,

* [`mergeNodes(T primary, T secondary)`](../javadocs/org/ghotibeaun/json/merge/MergeProcess.html#mergeNodes(T,T)) to perform a merge between
a "primary" and "secondary" JSON instance, and returns a merged union of the primary and secondary instances. 

There are additional methods that provide support for handling merge conflicts. For more about conflicts, see [Managing Conflicts](#managing-conflicts)

* [`getArrayConflictStrategy()`](../javadocs/org/ghotibeaun/json/merge/MergeProcess.html#getArrayConflictStrategy()) - returns the defined 
  conflict strategy to apply to JSONArray values.
* [`getObjectConflictStrategy()`](../javadocs/org/ghotibeaun/json/merge/MergeProcess.html#getObjectConflictStrategy()) - returns the defined 
  conflict strategy to apply to JSONObject values

Finally the [`getMergeResultStrategy()`](../javadocs/org/ghotibeaun/json/merge/MergeProcess.html#getMergeResultStrategy()) defines whether the 
merge result should be returned as a new JSON instance, or as the updated primary JSON instance (here there be dragons - it's powerful and useful,
but the original primary JSON instance is replaced with the merged instance - **know your data**).

The [`MergeProcessor`](../javadocs/org/ghotibeaun/json/merge/MergeProcessor.html) is a built-in implementation of the `MergeProcess` interface,
and includes several static `merge` methods that support several merge scenarios. Some of these `merge` scenarios allow you to define specific
[conflict strategies](#managing-conflicts) for handling merge conflicts for JSONArrays and JSONObjects:

* [ArrayConflict](../javadocs/org/ghotibeaun/json/merge/strategies/ArrayConflict.html) - specifies the conflict strategy options
  available to apply to JSONArrays. Each option is mapped to a specific [`JSONArrayConflictStrategy`](../javadocs/org/ghotibeaun/json/merge/strategies/JSONArrayConflictStrategy.html) implementation:

    * [`APPEND`](#appendarrayconflictstrategy)
    * [`DEDUPLICATE`](#deduplicatearrayconflictstrategy)
    * [`INSERT_BEFORE`](#insertbeforeconflictstrategy)
    * [`INSERT_AFTER`](#insertafterconflictstrategy)

* [ObjectConflict](../javadocs/org/ghotibeaun/json/merge/strategies/ObjectConflict.html) - specifies the available conflict options that can be 
  applied to JSONObjects. Each option is mapped to a specific [`JSONObjectConflictStrategy`](../javadocs/org/ghotibeaun/json/merge/strategies/JSONObjectConflictStrategy.html) implementation:

    * [`ACCEPT_PRIMARY`](#acceptprimaryconflictstrategy)
    * [`ACCEPT_SECONDARY`](#acceptsecondaryconflictstrategy)
    * [`APPEND`](#appendobjectconflictstrategy)
    

## Basic Merging Scenarios

There are cases when you need to merge data from two JSON instances. For example:

**JSON Instance 1:**

```json
{
    "key1": "value1",
    "key2": "value2"
}
```

**JSON Instance 2:**

```json
{
    "key3": "value3",
    "key4": "value4"
}
```

**Merged Instance:**

```json
{
    "key1": "value1",
    "key2": "value2",
    "key3": "value3",
    "key4": "value4"
}
```

The same merge process works for a JSONArray:

```json
[
    "value1",
    "value2"
]
```

```json
[
    "value3",
    "value4"
]
```
Results in:

```json
[
    "value1",
    "value2",
    "value3",
    "value4"
]
```

Similarly, merging is relatively straightforward when the following is true:

1. Both JSONObject instances have equivalent properties (e.g., the keys and values match).
2. Both JSONArray instances have equivalent values _at the same index position within both arrays_.

If either of these conditions is true, then the merged instance simply incorporates the property (for JSONObjects), or value at the
indexed position (for JSONArrays).

```json
{
    "pi": 3.14,
    "phi": 1.62,
    "theodorus": 1.73
}
```

```json
{
    "pi": 3.14,
    "phi": 1.62,
    "pythag": 1.414
}
```

Would return a merged result of:

```json
{
    "pi": 3.14,
    "phi": 1.62,
    "theodorus": 1.73,
    "pythag": 1.414
}
```
Similarly, with a JSONArray merge, if the values are equivalent at the same index position, then the merge
will merge both values:

```json
[
    3.14,
    1.62
    1.73
]
```

```json
[
    3.14,
    1.62,
    1.414
]
```

Would return a merged result of:

```json
[
    3.14,
    1.62,
    1.73,
    1.414
]
```

## A Few More Complex Examples

Up until now, we've discussed merging of simple, primitives (i.e., Strings, numbers, etc.). So what would a merge look like in
the case of nested JSONObject or JSONArray values?  

```json
{
    "team": {
        "name": "Boston Red Sox",
        "league": "American League"
    }
}
```

```json
{
    "team": {
        "name": "Boston Red Sox",
        "stadium": "Fenway Park",
        "yearBuilt": 1912
    }
}
```

Since both JSON instances have a `team` property AND the values in both are the _same_ value type (JSONObject), then we can merge the `team` property into
a single JSONObject value:

```json
{
    "team": {
        "name": "Boston Red Sox",
        "league": "American League",
        "stadium": "Fenway Park",
        "yearBuilt": 1912        
    }
}
```
Let's look at a JSONArray example:

```json
[
    {
        "name": "Boston Red Sox",
        "league": "American League"
    },
    {
        "name": "New York Yankees",
        "league": "American League"
    }
]
```

```json
[
    {
        "name": "Boston Red Sox",
        "stadium": "Fenway Park",
        "yearBuilt": 1912        
    },
    {
        "name": "New York Yankees",
        "stadium": "Yankee Stadium",
        "yearBuilt": 2009
    }
]
```

Since each value in both JSONArray instances are the same type (JSONObject), we can merge the values:

```json

[
    {
        "name": "Boston Red Sox",
        "league": "American League",
        "stadium": "Fenway Park",
        "yearBuilt": 1912        
    },
    {
        "name": "New York Yankees",
        "league": "American League",
        "stadium": "Yankee Stadium",
        "yearBuilt": 2009
    }
]    
```

## A Brief Discussion On Equivalency

There are three levels of equivalency in use for JSON nodes. This is important as this sets the core assumptions for how
we handle non-equivalencies and conflicts. 

1. A `JSONValue` is equivalent if:
    
    * Both values are the same type (`JSONValueType`)
    * For primitive values, they can be evaluated to be equal from the `Object.equals()` method or
      similarly evaluated as equal through the `compare()` method from the `Comparable<T>` interface.
      For example `true == true`, `234 == 234`, `"foo".equals("foo")`, `null == null`
    * For `JSONObject` or `JSONArray` values, the `isEquivalent(JSONNode)` method returns `true`. 

2. A `JSONObject` is equivalent to another if:
    
    * Both JSONObjects contain exactly the same _keys_
    * The values (`JSONValue`) for each key are equivalent
    * The order of the keys does _not_ matter
    * Example: `{"a": "foo", "b": "bar"} == {"b": "bar", "a": "foo"}`

3. A `JSONArray` is equivalent to another if:

    * Both JSONArray instances contain the same number of elements 
    * The values (`JSONValue`) at each index position are equivalent
    * The order of the elements in the array _does_ matter
    * Example: `[1, 3, 5] == [1, 3, 5]`. However, `[1, 3, 5] != [1, 5, 3]`
    
> **IMPORTANT**: Equivalency in JSONObjects and JSONArrays is recursive. This means that the _entire_
> structure of each of the JSONNodes compared must be equivalent using the conditions set out above.
> If any of these conditions at any point in the structure is not true, then the entire structure
> is not equivalent.

## Managing Conflicts

Up to this point, we've discusssed merge scenarios where properties were either distinctly equivalent, or distinctly different.
For example, in the case of a JSONObject, we might have key/value properties where the values are the same:

```json
{"foo": "bar"}

{"foo": "bar"}
```

Returns:
```json
{"foo": "bar"}
```

Likewise, we've discussed how a merge handles distinctly different keys:

```json
{"foo": "bar"}

{"bar": "baz"}
```

Returns:
```json
{"foo": "bar", "bar": "baz"}
```

However, what if we attempt to merge two JSON instances with the same keys but non-equivalent values? Remember that we defined 
[equivalency](#a-brief-discussion-on-equivalency) both in terms of _type_ (via the `getType()` value on the `JSONValue` interface),
and the underlying value. In the case where the values are both either a `JSONObject` or `JSONArray` type, then we'll merge the two values;
however if the types are different, or the values are _primitive_ types AND are not equal, then we need to apply a conflict strategy to
address them.

For JSONObject instances with the same key, but different values, we could choose a conflict strategy that declares:

* The primary value wins, or...
* The secondary value wins, or...
* We split the difference, create a new key, and apply the conflicted value to it

These are different strategies that you might choose depending on your data, and the underlying business requirements.

Likewise, we could apply different strategies for conflicts in JSONArrays. Here a conflict occurs when the values a given index position
exists in both the primary and secondary arrays, but are different, or the value at a given index position in the primary does not exist
in the seconary, or vice versa. Since order matters within a merged JSONArray, there several ways to handle conflicts:

* Append to the end:  append all conflicting values to the end of the array
* Insert Before: Insert the conflicting value immediately before the primary value
* Insert After: Insert the conflicting value immediately before the primary value

There's a fourth option, which really is a special case: We could choose to deduplicate values so that our merged array contains
only distinct values. 

## The ConflictStrategy Interface

[`ConflictStrategy`](../javadocs/org/ghotibeaun/json/merge/strategies/ConflictStrategy.html) is the base interface for handling conflicts for either a
`JSONArray` or `JSONObject` instance and defines a single method to resolve conflicts:

* [`apply(T context, G propertyItem, JSONValue primaryValue, JSONValue secondaryValue)`](../javadocs/org/ghotibeaun/json/merge/strategies/ConflictStrategy.html#apply(T,G,org.ghotibeaun.json.JSONValue,org.ghotibeaun.json.JSONValue)) - applies the designate conflict strategy to the primary and secondary values,
  and sets the _resolved_ value on the context JSONNode. The `propertyItem` will be either a String key, if the context element is a `JSONObject`,
  or an Integer index value for a `JSONArray`. This method is called within a `mergeNodes` method from a `MergeProcess` instance. 

The interface also defines the [`getMergeProcessor()`](../javadocs/org/ghotibeaun/json/merge/strategies/ConflictStrategy.html#getMergeProcessor()) method
that returns the `MergeProcess` instance that created this instance.

Each `ConflictStrategy` is type-specific, meaning that it will only apply to `JSONObject` or `JSONArray` context. For this, there are distinct
subinterfaces for each type:

* [`JSONArrayConflictStrategy`](../javadocs/org/ghotibeaun/json/merge/strategies/JSONArrayConflictStrategy.html) - applied to JSONArray instances
* [`JSONObjectConflictStrategy`](../javadocs/org/ghotibeaun/json/merge/strategies/JSONObjectConflictStrategy.html) - applied to JSONObject instances.

For each of these subinterfaces, there are abstract implementations that handle the basic block and tacking of creating instances that return the 
`MergeProcess`. 

### JSONObject Conflict Strategies

There are three concrete implementations of the `JSONObjectConflictStrategy` interface. Each addresses merge conflicts differently:

#### [AcceptPrimaryConflictStrategy](../javadocs/org/ghotibeaun/json/merge/strategies/AcceptPrimaryConflictStrategy.html) 

This strategy will use the value from the primary JSONObject instance in the event of a merge conflict for a given property key. Example:

  ```json
  //primary
  {
      "foo": "bar"
  }

  //secondary
  {
      "foo": "baz"
  }
  ```
  returns value from the primary:
  ```json
  {
      "foo": "bar"
  }
  ```
#### [AcceptSecondaryConflictStrategy](../javadocs/org/ghotibeaun/json/merge/strategies/AcceptSecondaryConflictStrategy.html) 
This strategy uses the value from the secondary JSONObject instance in the event of a merge conflict for a given property key. It's the reverse of the `AcceptPrimaryConflictStrategy`. Example:

```json
//primary
{
    "foo": "bar"
}

//secondary
{
    "foo": "baz"
}
```
returns value from the secondary:
```json
{
    "foo": "baz"
}
```

#### [AppendObjectConflictStrategy](../javadocs/org/ghotibeaun/json/merge/strategies/AppendObjectConflictStrategy.html) 

In the event of a merge conflict,this strategy appends both the primary value using the primary key, and the secondary value using a new key.  The new key value is a concatenation of the orginal key and the factory setting from the [`MERGE_APPEND_KEY`](../javadocs/org/ghotibeaun/json/factory/Setting.html#MERGE_APPEND_KEY). The default value is `_append`. You can change this value using the [`FactorySettings`](../javadocs/org/ghotibeaun/json/factory/FactorySettings.html)`.applySetting(Setting.MERGE_APPEND_KEY, "[new_value]")` method. Example:

```json
//primary
{
    "foo": "bar"
}

//secondary
{
    "foo": "baz"
}
```

returns:

```json
{
    "foo": "bar",
    "foo_append": "baz"
}
```

### JSONArray Conflict Strategies

There are four defined conflict strategies that can be applied to JSONArray instances that implement the `JSONArrayConflictStrategy` interface:

#### [AppendArrayConflictStrategy](../javadocs/org/ghotibeaun/json/merge/strategies/AppendArrayConflictStrategy.html)

Merge conflicts are appended to the end of the new array. Example:

```json
//primary
[
    1,
    2,
    3
]
//secondary
[
    1,
    3,
    4
]
```

returns an an array where the conflicting values (3, 4) are appended to the end. Note that the value 3 is included twice:
```json
[
    1,
    2,
    3,
    3,
    4
]
```

This also applies to arrays of different size:

```json
//primary
[
    1,
    2,
    3
]

//secondary
[
    3,
    4
]
```

returns:

```json
[
    1,
    2,
    3,
    3,
    4
]
```
#### [InsertBeforeConflictStrategy](../javadocs/org/ghotibeaun/json/merge/strategies/InsertBeforeConflictStrategy.html)

Merge conflicts at each index position are inserted before the primary value. Example:

```json
//primary
[
    1,
    2,
    3,
]
//secondary
[
    1,
    3,
    4
]
```

returns:

```json
[
    1,
    3,
    2,
    4,
    3
]
```
#### [InsertAfterConflictStrategy](../javadocs/org/ghotibeaun/json/merge/strategies/InsertAfterConflictStrategy.html) 

Merge conflicts at each index position are inserted after the primary value. Example:

```json
//primary
[
    1,
    2,
    3,
]
//secondary
[
    1,
    4,
    6
]
```

returns:

```json
[
    1,
    2,
    4,
    3,
    6
]
```
#### [DeduplicateArrayConflictStrategy](../javadocs/org/ghotibeaun/json/merge/strategies/DeduplicateArrayConflictStrategy.html) 

Creates a merged array that includes only distinct values from both arrays. Note that order is not necessarily sorted.

```json
//primary
[
    1,
    2,
    3,
]
//secondary
[
    1,
    3,
    4
]
```

returns:

```json
[
    1,
    2,
    3,
    4
]
```

