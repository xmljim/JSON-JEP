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

import org.ghotibeaun.json.JSONValueType;

public class Column {

    private String columnName;
    private JSONValueType type;
    private int columnPosition;
    private boolean nullable;

    /**
     * Create a column definition. By default, it sets `nullable` to `true`, and
     * the `type` to `UNKNOWN`
     * @param columnName the columnName
     * @param position the column position
     */
    public Column(String columnName, int position) {
        setColumnName(columnName);
        setPosition(position);
        setType(JSONValueType.UNKNOWN);
        setNullable(true);
    }

    /**
     * Create a column definition. Sets the `type` to `UNKNOWN`
     * @param columnName the column name
     * @param position the column position (zero-based)
     * @param nullable specifies whether the data for this column can have null values - if set to false and a null/empty value occurs
     * an error will be thrown
     */
    public Column(String columnName, int position, boolean nullable) {
        this(columnName, position);
        setNullable(nullable);
    }

    /**
     * Create a column definition
     * @param columnName the column name
     * @param position the column position
     * @param nullable specifies whether data for this column can have null values. If set to false and a null/empty value occurs, an
     * error will be thrown
     * @param type the data type
     */
    public Column(String columnName, int position, boolean nullable, JSONValueType type) {
        this(columnName, position, nullable);
        setType(type);
    }

    /**
     * Set the column name
     * @param columnName the column name
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName.replaceAll("\\s", "_");
    }

    /**
     * Get the column name
     * @return the column name
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Set the column data type
     * @param type the data type
     */
    public void setType(JSONValueType type) {
        this.type = type;
    }

    /**
     * get the column data type
     * @return the column data type
     */
    public JSONValueType getType() {
        if (type == null) {
            return JSONValueType.UNKNOWN;
        } else {
            return type;
        }
    }

    /**
     * Set the column position (zero-based)
     * @param position the column position
     */
    public void setPosition(int position) {
        columnPosition = position;
    }

    /**
     * Get the column position (zero-based)
     * @return the column position
     */
    public int getPosition() {
        return columnPosition;
    }  

    /**
     * Sets whether the column can have null values. This is useful for data and number fields, but can be used for String fields as well.
     * @param nullable sets whether column can have null values
     */
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * Gets whether column can have null values.
     * @return true if the field can have null values; false otherwise. For Strings, if the value is false, the expectation is to create
     * an empty String value.  For numbers, if the value is false, then it is expected to return 0. For dates, if the date is false,
     * an error will occur.
     */
    public boolean isNullable() {
        return nullable;
    }

}
