package org.ghotibeaun.json;

import java.util.Iterator;
import java.util.List;

public interface JSONListNode extends JSONNode, Iterable<JSONValue<?>> {

    void add(JSONValue<?> value);

    void addAll(List<?> list);

    void insert(int index, JSONValue<?> value);

    void insert(int index, Object value);

    JSONValue<?> get(int index);

    @Override
    Iterator<JSONValue<?>> iterator();

    JSONValue<?> remove(int index);


    void clear();

    int size();

    List<JSONValue<?>> getValues();

    <V> List<V> toList();

    public default boolean isNull(int index) {
        return this.get(index).getType() == JSONValueType.NULL;
    }

    List<Object> getList();
}
