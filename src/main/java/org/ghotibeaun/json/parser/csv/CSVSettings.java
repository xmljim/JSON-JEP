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

import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValueType;
import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.factory.Setting;
import org.ghotibeaun.json.parser.JSONParser;
import org.ghotibeaun.json.parser.ParserFactory;

public class CSVSettings implements Iterator<Column> {

    private final ArrayList<Column> columns = new ArrayList<>();
    private final Map<String, Column> columnMap = new HashMap<>();

    private char separator = ',';
    private char enclosure = '"';
    private char escape = '\\';
    private boolean headerRow = true;
    private String dateFormat = "";
    private boolean defaultSettings;
    private int index = -1;

    public CSVSettings() {

    }

    public static CSVSettings getDefaultSettings() {
        final CSVSettings settings = new CSVSettings();
        settings.defaultSettings = true;
        return settings;
    }

    public static CSVSettings newSettings() {
        final CSVSettings settings = new CSVSettings();
        settings.defaultSettings = false;
        return settings;
    }

    public static CSVSettings newSettings(Column...columns) {
        final CSVSettings settings = newSettings();
        for (final Column c : columns) {
            settings.addColumnDefinition(c);
        }

        return settings;
    }

    public static CSVSettings fromConfiguration(Path configurationPath) {
        final JSONParser parser = ParserFactory.getParser();
        final JSONNode config = parser.parse(configurationPath);
        return fromConfiguration(config.asJSONObject());
    }

    public static CSVSettings fromConfiguration(InputStream inputStream) {
        return fromConfiguration(ParserFactory.getParser().parse(inputStream).asJSONObject());
    }

    public static CSVSettings fromConfiguration(JSONObject configuration) {
        CSVSettings settings = null;
        if (configuration.containsKey("columns")) {
            settings = newSettings();

        } else {
            settings = getDefaultSettings();
        }

        if (configuration.containsKey("separator")) {
            settings.setSeparator(configuration.getString("separator").charAt(0));
        }

        if (configuration.containsKey("enclosure")) {
            settings.setEnclosure(configuration.getString("enclosure").charAt(0));
        }

        if (configuration.containsKey("escape")) {
            settings.setEscape(configuration.getString("escape").charAt(0));
        }

        if (configuration.containsKey("headerRow")) {
            settings.setHeaderRow(configuration.getBoolean("headerRow"));
        }

        if (configuration.containsKey("dateFormat")) {
            settings.setDateFormat(configuration.getString("dateFormat"));
            FactorySettings.applySetting(Setting.DATE_FORMAT, configuration.getString("dateFormat"));
        }

        if (configuration.containsKey("columns")) {
            final JSONArray columnSet = configuration.getJSONArray("columns");

            for (int i = 0; i < columnSet.size(); i++) {
                final JSONObject columnObj = columnSet.getJSONObject(i);
                final String columnName = columnObj.getString("columnName");
                final boolean nullable = columnObj.containsKey("nullable") ? columnObj.getBoolean("nullable") : true;
                final JSONValueType type = columnObj.containsKey("type") ? JSONValueType.valueOf(columnObj.getString("type")) : JSONValueType.UNKNOWN;
                settings.addColumnDefinition(columnName, nullable, type);
            }
        }

        return settings;
    }

    public void addColumnDefinition(String columnName) {
        addColumnDefinition(columnName, true);
    }

    public void addColumnDefinition(Column column) {
        columnMap.put(column.getColumnName(), column);
        columns.add(column);
    }

    public void addColumnDefinition(String columnName, boolean nullable) {
        final int position = columns.size();
        final Column column = new Column(columnName, position, nullable);
        addColumnDefinition(column);
    }

    public void addColumnDefinition(String columnName, boolean nullable, JSONValueType type) {
        final int position = columns.size();
        final Column column = new Column(columnName, position, nullable, type);
        addColumnDefinition(column);
    }

    public Iterator<Column> iterator() {
        return columns.iterator();
    }

    public void setSeparator(char separator) {
        this.separator = separator;
    }

    public char getSeparator() {
        return separator;
    }

    public byte getSeparatorByte() {
        return (byte)separator;
    }

    public void setEnclosure(char enclosure) {
        this.enclosure = enclosure;
    }

    public char getEnclosure() {
        return enclosure;
    }

    public byte getEnclosureByte() {
        return (byte)getEnclosure();
    }

    public void setEscape(char escape) {
        this.escape = escape;
    }

    public char getEscape() {
        return escape;
    }

    public byte getEscapeByte() {
        return (byte)getEscape();
    }

    public void setHeaderRow(boolean hasHeaderRow) {
        headerRow = hasHeaderRow;
    }

    public boolean getHeaderRow() {
        return headerRow;
    }

    public void setDateFormat(String format) {
        dateFormat = format;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public boolean isDefault() {
        return defaultSettings;
    }

    @Override
    public boolean hasNext() {
        final int next = index + 1;
        return next < columns.size();

    }

    @Override
    public Column next() {
        if (hasNext()) {
            index++;
            return columns.get(index);
        } else {
            return null;
        }
    }

    public void reset() {
        index = -1;
    }
}
