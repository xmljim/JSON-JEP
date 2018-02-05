package org.ghotibeaun.json.factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ghotibeaun.json.JSONListNode;
import org.ghotibeaun.json.JSONValue;

abstract class AbstractListNode extends AbstractJSONNode implements JSONListNode, Iterable<JSONValue<?>> {

    private final List<JSONValue<?>> jsonList = new ArrayList<JSONValue<?>>();

    public AbstractListNode() {
        super();
    }

    @Override
    public void add(JSONValue<?> value) {
        jsonList.add(value);
    }

    @Override
    public void insert(int index, JSONValue<?> value) {
        jsonList.add(index, value);
    }

    @Override
    public void insert(int index, Object value) {
        jsonList.add(index, NodeFactory.createFromObject(value));
    }

    @Override
    public JSONValue<?> get(int index) {
        return jsonList.get(index);
    }

    @Override
    public Iterator<JSONValue<?>> iterator() {
        return jsonList.iterator();
    }

    @Override
    public JSONValue<?> remove(int index) {
        return jsonList.remove(index);
    }

    @Override
    public void clear() {
        jsonList.clear();

    }

    @Override
    public void addAll(List<?> list) {
        for (final Object val : list) {
            final JSONValue<?> value = NodeFactory.createFromObject(val);
            add(value);
        }

    }

    @Override
    public int size() {
        return jsonList.size();
    }

    @Override
    public List<JSONValue<?>> getValues() {
        return jsonList;
    }

    @Override
    public JSONValue<?> getValue() {
        // TODO Auto-generated method stub
        return null;
    }

}
