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
package org.ghotibeaun.json.parser.csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.factory.NodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Row implements Iterable<Field>{
    private static Logger LOGGER = LoggerFactory.getLogger(Row.class);

    private final List<Field> fields = new ArrayList<>();
    private final Map<String, Field> fieldMap = new HashMap<>();

    private final int rowNumber;

    public Row(int rowNumber) {
        this.rowNumber = rowNumber;
    }


    public void appendField(Column column, String data, CSVSettings settings) {
        final Field f = new Field(column, data, settings);
        appendField(f);
    }

    public void appendField(Field field) {
        fields.add(field);
        fieldMap.put(field.getName(), field);
    }

    public Field getField(int index) {
        return fields.get(index);
    }

    public Field getField(String name) {
        return fieldMap.get(name);
    }

    public JSONObject getJSONObject() {
        final JSONObject obj = NodeFactory.newJSONObject();
        obj.put("@rowNumber", rowNumber);

        for (final Field f : fields) {
            obj.put(f.getName(), f.getValue());
        }
        LOGGER.debug("JSONObject: {}", obj.prettyPrint());
        return obj;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    @Override
    public Iterator<Field> iterator() {
        return fields.iterator();
    }
}
