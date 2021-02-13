package org.ghotibeaun.json.marshalling;

import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.exception.JSONUnmarshallingException;

public interface JSONUnmarshaller {

    JSONObject unmarshal(Object instance) throws JSONUnmarshallingException;
}
