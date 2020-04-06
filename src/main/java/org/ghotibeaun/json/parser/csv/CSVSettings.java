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
import org.ghotibeaun.json.parser.JSONParser;
import org.ghotibeaun.json.parser.ParserFactory;

public class CSVSettings implements Iterator<Column> {

    private ArrayList<Column> columns = new ArrayList<>();
    private Map<String, Column> columnMap = new HashMap<>();
    
    private char separator = ',';
    private char enclosure = '"';
    private char escape = '\\';
    private boolean headerRow = true;
    private String dateFormat = "";
    private boolean defaultSettings;
    private int index = -1;
    
    private CSVSettings() {
        
    }
    
    public static CSVSettings getDefaultSettings() {
        CSVSettings settings = new CSVSettings();
        settings.defaultSettings = true;
        return settings;
    }
    
    public static CSVSettings newSettings() {
        CSVSettings settings = new CSVSettings();
        settings.defaultSettings = false;
        return settings;
    }
    
    public static CSVSettings newSettings(Column...columns) {
        CSVSettings settings = newSettings();
        for (Column c : columns) {
            settings.addColumnDefinition(c);
        }
        
        return settings;
    }
    
    public static CSVSettings fromConfiguration(Path configurationPath) {
        JSONParser parser = ParserFactory.getParser();
        JSONNode config = parser.parse(configurationPath);
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
            FactorySettings.applySetting(FactorySettings.JSON_DATE_FORMAT, configuration.getString("dateFormat"));
        }
        
        if (configuration.containsKey("columns")) {
            JSONArray columnSet = configuration.getJSONArray("columns");
            
            for (int i = 0; i < columnSet.size(); i++) {
                JSONObject columnObj = columnSet.getJSONObject(i);
                String columnName = columnObj.getString("columnName");
                boolean nullable = columnObj.containsKey("nullable") ? columnObj.getBoolean("nullable") : true;
                JSONValueType type = columnObj.containsKey("type") ? JSONValueType.valueOf(columnObj.getString("type")) : JSONValueType.UNKNOWN;
                settings.addColumnDefinition(columnName, nullable, type);
            }
        }
        
        return settings;
    }
    
    public void addColumnDefinition(String columnName) {
        addColumnDefinition(columnName, true);
    }
    
    public void addColumnDefinition(Column column) {
        this.columnMap.put(column.getColumnName(), column);
        this.columns.add(column);
    }
    
    public void addColumnDefinition(String columnName, boolean nullable) {
        int position = columns.size();
        Column column = new Column(columnName, position, nullable);
        addColumnDefinition(column);
    }
    
    public void addColumnDefinition(String columnName, boolean nullable, JSONValueType type) {
        int position = columns.size();
        Column column = new Column(columnName, position, nullable, type);
        addColumnDefinition(column);
    }
    
    public Iterator<Column> iterator() {
        return columns.iterator();
    }
    
    public void setSeparator(char separator) {
        this.separator = separator;
    }
    
    public char getSeparator() {
        return this.separator;
    }
    
    public byte getSeparatorByte() {
        return (byte)separator;
    }
    
    public void setEnclosure(char enclosure) {
        this.enclosure = enclosure;
    }
    
    public char getEnclosure() {
        return this.enclosure;
    }
    
    public byte getEnclosureByte() {
        return (byte)getEnclosure();
    }
    
    public void setEscape(char escape) {
        this.escape = escape;
    }
    
    public char getEscape() {
        return this.escape;
    }
    
    public byte getEscapeByte() {
        return (byte)getEscape();
    }
    
    public void setHeaderRow(boolean hasHeaderRow) {
        this.headerRow = hasHeaderRow;
    }
    
    public boolean getHeaderRow() {
        return this.headerRow;
    }
    
    public void setDateFormat(String format) {
        this.dateFormat = format;
    }
    
    public String getDateFormat() {
        return this.dateFormat;
    }
    
    public boolean isDefault() {
        return defaultSettings;
    }

    @Override
    public boolean hasNext() {
        int next = index + 1;
        return next < columns.size();
        
    }

    @Override
    public Column next() {
        if (hasNext()) {
            index++;
            return this.columns.get(index);
        } else {
            return null;
        }
    }
    
    public void reset() {
        index = -1;
    }
}
