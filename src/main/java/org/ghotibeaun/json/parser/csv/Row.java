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
    
    private List<Field> fields = new ArrayList<>();
    private Map<String, Field> fieldMap = new HashMap<>();
    
    private final int rowNumber;
    
    public Row(int rowNumber) {
        this.rowNumber = rowNumber;
    }
    
    
    public void appendField(Column column, String data, CSVSettings settings) {
        Field f = new Field(column, data, settings);
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
        JSONObject obj = NodeFactory.newJSONObject();
        obj.put("@rowNumber", rowNumber);
        
        for (Field f : fields) {
            obj.put(f.getName(), f.getValue());
        }
        LOGGER.debug("JSONObject: {}", obj.prettyPrint());
        return obj;
    }
    
    public int getRowNumber() {
        return this.rowNumber;
    }
    
    public Iterator<Field> iterator() {
        return fields.iterator();
    }
}
