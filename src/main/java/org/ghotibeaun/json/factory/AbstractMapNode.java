package org.ghotibeaun.json.factory;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ghotibeaun.json.JSONMapNode;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.exception.JSONInvalidValueTypeException;

abstract class AbstractMapNode extends AbstractJSONNode implements JSONMapNode {

    private final Map<String, JSONValue<?>> jsonMap = new LinkedHashMap<String, JSONValue<?>>();

    public AbstractMapNode() {
        // no-op
    }

    @Override
    public void put(String key, JSONValue<?> value) {
        jsonMap.put(key, value);

    }

    @Override
    public void put(String key, Object value) {
        final JSONValue<?> v = NodeFactory.createFromObject(value);
        if (v == null) {
            throw new JSONInvalidValueTypeException(value.getClass().getName() + " is not a valid value type");
        }
        put(key, v);

    }

    @Override
    public void putAll(Map<String, JSONValue<?>> map) {
        jsonMap.putAll(map);

    }

    @Override
    public void putAllRaw(Map<String, Object> map) {
        final Set<String> keys = map.keySet();
        for (final String key : keys) {
            final JSONValue<?> v = NodeFactory.createFromObject(map.get(key));
            put(key, v);
        }

    }

    @Override
    public void clear() {
        jsonMap.clear();

    }

    @Override
    public int size() {
        return jsonMap.size();
    }

    @Override
    public Collection<JSONValue<?>> values() {
        return jsonMap.values();
    }

    @Override
    public Iterable<String> keys() {
        return jsonMap.keySet();
    }

    @Override
    public JSONValue<?> get(String key) {

        return jsonMap.containsKey(key) ? jsonMap.get(key) : null;
    }

    @Override
    public Map<String, Object> getMap() {
        final Map<String, Object> map = new LinkedHashMap<String, Object>();
        for (final Entry<String, JSONValue<?>> element : elements()) {
            map.put(element.getKey(), element.getValue().getValue());
        }

        return map;
    }

    @Override
    public Set<Entry<String, JSONValue<?>>> elements() {
        return jsonMap.entrySet();
    }

    @Override
    public String[] names() {
        final String[] values = jsonMap.keySet().toArray(new String[size()]);
        return values;
    }

    @Override
    public boolean containsKey(String key) {
        return jsonMap.containsKey(key);
    }

    @Override
    public JSONValue<?> remove(String key) {
        return jsonMap.remove(key);
    }

    @Override
    public JSONValue<?> getValue() {
        // TODO Auto-generated method stub
        return null;
    }

}
