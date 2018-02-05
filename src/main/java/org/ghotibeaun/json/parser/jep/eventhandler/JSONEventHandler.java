package org.ghotibeaun.json.parser.jep.eventhandler;

import java.math.BigDecimal;
import java.nio.charset.Charset;

import org.ghotibeaun.json.JSONValueType;
import org.ghotibeaun.json.parser.jep.Configurable;
import org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEvent;

public interface JSONEventHandler extends Configurable {
    Charset getCharacterSet();

    
    void handleEvent(JSONEvent event);

    /**
     * The document start event
     * @param type the document type, will either be {@linkplain JSONValueType#OBJECT} or
     * {@link JSONValueType#ARRAY}
     */
    void documentStart(JSONValueType type);
    
    /**
     * The document end event
     */
    void documentEnd();
    
    /**
     * Start of new JSONArray.
     * @param key the parent key that is associated with the array
     */
    void jsonArrayStart(String key);
    
    void jsonArrayEnd(String key);
    
    void jsonObjectStart(String key);
    
    void jsonObjectEnd(String key);
    
    void valueString(String key, String value);
    
    void valueLong(String key, Long value);
    
    void valueInt(String key, Integer value);
    
    void valueBigDecimal(String key, BigDecimal value);
    
    void valueBoolean(String key, boolean value);
    
    void valueNull(String key);
    
    void newKey(String key);
}
