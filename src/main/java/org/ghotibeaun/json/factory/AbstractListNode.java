package org.ghotibeaun.json.factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONListNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.converters.Converters;

abstract class AbstractListNode extends AbstractJSONNode implements JSONListNode, Iterable<JSONValue<?>> {

    /**
     *
     */
    private static final long serialVersionUID = -8439023075355492351L;
    private final List<JSONValue<?>> jsonList = new ArrayList<>();

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
        jsonList.add(index, Converters.convertToJSONValue(value, Optional.empty(), Optional.empty()));  //NodeFactory.createFromObject(value));
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
            final JSONValue<?> value = Converters.convertToJSONValue(val, Optional.empty(), Optional.empty());//NodeFactory.createFromObject(val);
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

    @Override
    public List<Object> getList() {
        final List<Object> theList = new ArrayList<>();

        for (final JSONValue<?> val : getValues()) {
            if (val.isPrimitive()) {
                theList.add(val.getValue());
            } else if (val.isArray()) {
                theList.add(((JSONArray)val.getValue()).getList());
            } else {
                theList.add(((JSONObject)val.getValue()).getMap());
            }
        }

        return theList;
    }

}
