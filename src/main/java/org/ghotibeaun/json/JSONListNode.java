package org.ghotibeaun.json;

import java.util.Iterator;
import java.util.List;

/**
 * Base interface for managing list/array values
 * @author Jim Earley (jim.earley@fdiinc.com)
 *
 */
public interface JSONListNode extends JSONNode, Iterable<JSONValue<?>> {

    /**
     * Add a JSONValue
     * @param value the JSONValue
     */
    void add(JSONValue<?> value);

    /**
     * A a list of values. Each value is evaluated and converted to a JSON value
     * @param list the list containing the values
     */
    void addAll(List<?> list);

    /**
     * Insert a JSONValue at a specified position
     * @param index the position with the list
     * @param value the value
     */
    void insert(int index, JSONValue<?> value);

    /**
     * Insert a value at specified position. The value will be converted to a JSONValue
     * @param index the position within the list to insert the value
     * @param value the raw value to be converted
     */
    void insert(int index, Object value);

    /**
     * Return a JSONValue at a specified position
     * @param index the position within the list of values
     * @return the JSONValue
     */
    JSONValue<?> get(int index);

    /**
     * Returns the iterator for this list.
     * Allows for standard 'for' loops, and streaming with the {@link #spliterator()}
     */
    @Override
    Iterator<JSONValue<?>> iterator();

    /**
     * Remove a value at a specified position
     * @param index the position within the list
     * @return the JSONValue removed
     */
    JSONValue<?> remove(int index);

    /**
     * Clear the list
     */
    void clear();

    /**
     * Return the current length of the list
     * @return the current length of the list
     */
    int size();

    /**
     * Returns whether the list is empty
     * @return true if empty; false otherwise
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Return a list of JSONValues
     * @return a List of JSONValue objects
     */
    List<JSONValue<?>> getValues();

    /**
     * Converts a list of JSONValues into a list of values
     * @param <V> The internal type for each value
     * @param targetClass the targetClass for each value
     * @return the List of values
     */
    <V> List<V> toList(Class<?> targetClass);

    /**
     * Returns whether a value at a specified position is a null value
     * @param index the position within the list
     * @return true if the value is null; false otherwise
     */
    public default boolean isNull(int index) {
        return this.get(index).getType() == JSONValueType.NULL;
    }

    /**
     * Return a list of Objects. Primitive values will be converted accordingly. JSONArrays will be converted to List,
     * and JSONObject will be converted to Map<String, Object> 
     * @return a list of values
     */
    List<Object> getList();

    public default JSONValue<?> getLast() {
        if (size() > 0) {
            return get(size() - 1);
        } else {
            return null;
        }
    }

    /**
     * Return the first item in the list
     * @return the first item in the list, or null if the list is empty
     */
    public default JSONValue<?> getFirst() {
        if (size() > 0) {
            return get(0);
        } else {
            return null;
        }
    }
}
