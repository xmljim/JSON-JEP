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
import org.ghotibeaun.json.exception.JSONEventParserException;
import org.ghotibeaun.json.parser.jep.ParserSettings;
import org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEvent;

public abstract class EventHandler implements JSONEventHandler {
    private ParserSettings settings;

    public EventHandler() {

    }

    public static EventHandler consoleEventHandler() {
        return new ConsoleEventHandler();
    }

    @Override
    public void setParserSettings(ParserSettings settings) {
        this.settings = settings;
    }

    @Override
    public ParserSettings getParserSettings() {
        return settings;
    }

    @Override
    public Charset getCharacterSet() {
        return getParserSettings().getCharset();
    }

    @Override
    public abstract void documentStart(JSONValueType type);

    @Override
    public abstract void documentEnd();

    @Override
    public abstract void jsonArrayStart(String key);

    @Override
    public abstract void jsonArrayEnd(String key);

    @Override
    public abstract void jsonObjectStart(String key);

    @Override
    public abstract void jsonObjectEnd(String key);

    @Override
    public abstract void valueString(String key, String value);

    @Override
    public abstract void valueLong(String key, Long value);

    @Override
    public abstract void valueInt(String key, Integer value);

    @Override
    public abstract void valueBigDecimal(String key, BigDecimal value);

    @Override
    public abstract void valueDouble(String key, Double value);

    @Override
    public abstract void valueFloat(String key, Float value);

    @Override
    public abstract void valueBoolean(String key, boolean value);

    @Override
    public abstract void valueNull(String key);

    @Override
    public abstract void newKey(String key);

    @Override
    public abstract void handleEvent(JSONEvent event) throws JSONEventParserException;

    public String getDataValue(JSONEvent event) {
        String dataVal = null;
        if (event.getData() != null) {

            try {
                final String val = new String(event.getData().array(), this.getParserSettings().getCharset());
                //dataVal = decoder.decode(event.getData()).toString();
                //dataVal = new String(event.getData().array());
                dataVal = val;
            } catch (final Exception e) {
                throw new JSONEventParserException(e);
            }


        }

        return dataVal;
    }

}
