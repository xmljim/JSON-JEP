package org.ghotibeaun.json.factory;

import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONValue;

abstract class AbstractJSONNode implements JSONNode {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public AbstractJSONNode() {

    }

    public abstract JSONValue<?> getValue();

    @Override
    public String toJSONString() {
        return getValue().toString();
    }

    @Override
    public abstract String prettyPrint();

    @Override
    public abstract int compareTo(JSONNode other);

}
