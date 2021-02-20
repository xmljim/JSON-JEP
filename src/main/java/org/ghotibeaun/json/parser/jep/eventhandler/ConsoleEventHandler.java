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

import org.ghotibeaun.json.JSONValueType;
import org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEvent;

public class ConsoleEventHandler extends EventHandler {

    private int level;

    public ConsoleEventHandler() {

    }

    private void incrementLevel() {
        level++;
    }

    private void decrementLevel() {
        level--;
    }

    private int getLevel() {
        return level;
    }

    private String space() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < getLevel() * 4; i++) {
            builder.append(" ");
        }

        return builder.toString();
    }

    private void print(String data) {
        print(data, false);
    }

    private void print(String data, boolean indent) {
        final String extra = indent ? "    " : "";
        System.out.println(extra + space() + data);
    }

    @Override
    public void documentStart(JSONValueType type) {
        print("DOCUMENT START: (" + type.toString() + ")");

    }

    @Override
    public void documentEnd() {
        print("DOCUMENT END: \n----------\n");

    }

    @Override
    public void jsonArrayStart(String key) {
        incrementLevel();
        print("ARRAY START: (key=" + key + ")");

    }

    @Override
    public void jsonArrayEnd(String key) {
        print("ARRAY END: (key=" + key + ")");
        decrementLevel();

    }

    @Override
    public void jsonObjectStart(String key) {
        incrementLevel();
        print("OBJECT START: (key=" + key + ")");

    }

    @Override
    public void jsonObjectEnd(String key) {
        print("OBJECT END: (key=" + key + ")");
        decrementLevel();

    }

    @Override
    public void valueString(String key, String value) {
        print ("VALUE: key=" + key + "; value=" + value, true);

    }

    @Override
    public void valueLong(String key, Long value) {
        print("VALUE: key=" + key + "; value=" + value.toString(), true);

    }

    @Override
    public void valueInt(String key, Integer value) {
        print("VALUE: key=" + key + "; value=" + value.toString(), true);

    }

    @Override
    public void valueBigDecimal(String key, BigDecimal value) {
        print("VALUE: key=" + key + "; value=" + value.toString(), true);

    }

    @Override
    public void valueDouble(String key, Double value) {
        print("VALUE: key=" + key + "; value=" + value.toString(), true);

    }

    @Override
    public void valueFloat(String key, Float value) {
        print("VALUE: key=" + key + "; value=" + value.toString(), true);

    }

    @Override
    public void valueBoolean(String key, boolean value) {
        print("VALUE: key=" + key + "; value=" + value, true);

    }

    @Override
    public void valueNull(String key) {
        print("VALUE: key=" + key + "; value=null", true);

    }

    @Override
    public void newKey(String key) {
        print("{NEW KEY: " + key + "}",true);

    }

    @Override
    public void handleEvent(JSONEvent event) {
        print(event.toString());

    }

}
