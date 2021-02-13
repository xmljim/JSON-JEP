package org.ghotibeaun.json.marshalling;

import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.exception.JSONMarshallingException;

public abstract class AbstractJSONMarshaller implements JSONMarshaller {

    @Override
    public abstract <T> T marshall(Class<?> targetClass, JSONObject context) throws JSONMarshallingException;

    public static JSONMarshaller newMarshaller() {
        return new JSONMarshallerImpl();
    }
}
