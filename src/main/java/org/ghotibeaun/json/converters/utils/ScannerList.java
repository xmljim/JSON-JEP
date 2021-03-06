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
package org.ghotibeaun.json.converters.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.converters.ConverterOptions;
import org.ghotibeaun.json.factory.NodeFactory;

public class ScannerList {
    private final Map<String, ScannerEntry> entries = new HashMap<>();

    public ScannerList() {
        // TODO Auto-generated constructor stub
    }

    public void addEntry(Class<?> clazz, Field field, ConverterOptions options) {
        final ScannerEntry entry = new ScannerEntry(clazz, field, options);
        if (!entries.containsKey(entry.getJsonKey())) {
            entries.put(entry.getJsonKey(), entry);
        }
    }

    public Optional<ScannerEntry> getEntry(String jsonKey) {
        return Optional.ofNullable(entries.get(jsonKey));
    }

    public boolean hasEntry(String jsonKey) {
        return entries.containsKey(jsonKey);
    }

    public Iterable<String> keys() {
        return entries.keySet();
    }

    public Iterable<ScannerEntry> entries() {
        return entries.values();
    }

    public JSONObject toJSON() {
        final JSONObject scanner = NodeFactory.newJSONObject();
        for (final String key : entries.keySet()) {
            scanner.put(key, entries.get(key).toJSON());
        }

        return scanner;
    }
    @Override
    public String toString() {
        return toJSON().prettyPrint();
    }



}
