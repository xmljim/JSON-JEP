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
        for (int i = 0; i < (getLevel() * 4); i++) {
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
