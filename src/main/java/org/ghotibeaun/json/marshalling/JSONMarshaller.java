package org.ghotibeaun.json.marshalling;

import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.exception.JSONMarshallingException;

public interface JSONMarshaller {

    <T> T marshall(Class<?> targetClass, JSONObject context) throws JSONMarshallingException;


}
