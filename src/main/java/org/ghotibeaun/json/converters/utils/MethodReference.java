package org.ghotibeaun.json.converters.utils;

import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.factory.NodeFactory;

public abstract class MethodReference {

    private final String name;
    private final boolean accessible;
    private final boolean isPublic;

    public MethodReference(String name, boolean accessible, boolean isPublic) {
        this.name = name;
        this.accessible = accessible;
        this.isPublic = isPublic;
    }

    public String getName() {
        return name;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public JSONObject getJSON() {
        final JSONObject json = NodeFactory.newJSONObject();
        json.put("name", name);
        json.put("accessible", accessible);
        json.put("isPublic", isPublic);
        return json;
    }



}
