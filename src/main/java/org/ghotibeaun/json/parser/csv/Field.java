package org.ghotibeaun.json.parser.csv;

import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.factory.NodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Field {
    private static Logger LOGGER = LoggerFactory.getLogger(Field.class);
    private final Column column;
    private final String cellValue;
    private final CSVSettings settings;

    public Field(Column column, String fieldValue, CSVSettings settings) {
        this.column = column;
        cellValue = fieldValue;
        this.settings = settings;
    }

    public String getName() {
        if (column != null) {
            return column.getColumnName();
        } else {
            return cellValue;
        }
    }

    public String getFieldValue() {
        return cellValue;
    }

    public JSONValue<?> getValue() {
        JSONValue<?> val = null;
        if (cellValue == null) {
            val = NodeFactory.newJSONNullValue();
        } else if (cellValue.equals("")) {

            switch (column.getType()) {
                case STRING:
                    val = NodeFactory.newStringValue("");
                    break;
                default:
                    val = NodeFactory.newJSONNullValue();

            }
            return val;
        } else {
            switch (column.getType()) {
                case STRING:
                    val = NodeFactory.newStringValue(cellValue);
                    break;
                case BOOLEAN:
                    val = NodeFactory.newBooleanValue(Boolean.parseBoolean(cellValue));
                    break;
                case DATE:
                    val = NodeFactory.newStringValue(cellValue);
                    break;
                case NUMBER:
                    LOGGER.debug("NUMBER: {}:{}", column.getColumnName(), cellValue);
                    if (cellValue.contains(".")) {
                        val = NodeFactory.newNumberValue(Double.parseDouble(cellValue));
                    } else {
                        val = NodeFactory.newNumberValue(Integer.parseInt(cellValue));
                    }
                    break;
                default:
                    if (isBoolean()) {
                        val = NodeFactory.newBooleanValue(Boolean.parseBoolean(cellValue));
                    } else if (isNumeric()) {
                        if (cellValue.contains(".")) {
                            val = NodeFactory.newNumberValue(Double.parseDouble(cellValue));
                        } else {
                            val = NodeFactory.newNumberValue(Integer.parseInt(cellValue));
                        }
                    } else {
                        val = NodeFactory.newStringValue(cellValue);
                    }
                    break;

            }
        }


        return val;
    }

    private boolean isBoolean() {
        return cellValue.matches("true|false");
    }

    private boolean isNumeric() {
        return cellValue.matches("^(-)?[0-9]?\\d*(?:\\.\\d+)?$");
    }

    @SuppressWarnings("unused")
    private CSVSettings getSettings() {
        return settings;
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
