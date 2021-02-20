/*
 *
 * # Released under MIT License
 *
 * Copyright (c) 2016-2021 Jim Earley.
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 */
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

    void valueDouble(String key, Double value);

    void valueFloat(String key, Float value);

    void valueBoolean(String key, boolean value);

    void valueNull(String key);

    void newKey(String key);
}
