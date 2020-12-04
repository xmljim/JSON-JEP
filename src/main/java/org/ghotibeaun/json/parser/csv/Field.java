package org.ghotibeaun.json.parser.csv;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.factory.NodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Field {
    private static Logger LOGGER = LoggerFactory.getLogger(Field.class);
    private Column column;
    private String cellValue;
    private CSVSettings settings;
    
    public Field(Column column, String fieldValue, CSVSettings settings) {
        this.column = column;
        this.cellValue = fieldValue;
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
                    SimpleDateFormat sdf = new SimpleDateFormat(settings.getDateFormat());
                    try {
                        Date dateValue = sdf.parse(cellValue);
                        val = NodeFactory.newDateValue(dateValue);
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                        val = NodeFactory.newJSONNullValue();
                    }
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
    
    public String toString() {
        return getValue().toString();
    }
}