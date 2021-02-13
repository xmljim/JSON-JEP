package org.ghotibeaun.json.marshalling;

public abstract class MarshallingFactory {

    public static JSONMarshaller getJSONMarshaller() {
        return AbstractJSONMarshaller.newMarshaller();
    }
}
