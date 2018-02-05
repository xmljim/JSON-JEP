package org.ghotibeaun.json.parser.jep.eventhandler;

import java.math.BigDecimal;
import java.nio.charset.Charset;

import org.ghotibeaun.json.JSONValueType;
import org.ghotibeaun.json.exception.JSONEventParserException;
import org.ghotibeaun.json.parser.jep.ParserSettings;
import org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEvent;

public abstract class EventHandler implements JSONEventHandler {
    private ParserSettings settings;

    public EventHandler() {

    }

    public static EventHandler consoleEventHandler() {
        return new ConsoleEventHandler();
    }

    @Override
    public void setParserSettings(ParserSettings settings) {
        this.settings = settings;
    }

    @Override
    public ParserSettings getParserSettings() {
        return settings;
    }
    
    @Override
    public Charset getCharacterSet() {
        return getParserSettings().getCharset();
    }
    
    @Override
    public abstract void documentStart(JSONValueType type);

    @Override
    public abstract void documentEnd();

    @Override
    public abstract void jsonArrayStart(String key);

    @Override
    public abstract void jsonArrayEnd(String key);

    @Override
    public abstract void jsonObjectStart(String key);

    @Override
    public abstract void jsonObjectEnd(String key);

    @Override
    public abstract void valueString(String key, String value);

    @Override
    public abstract void valueLong(String key, Long value);

    @Override
    public abstract void valueInt(String key, Integer value);

    @Override
    public abstract void valueBigDecimal(String key, BigDecimal value);

    @Override
    public abstract void valueBoolean(String key, boolean value);

    @Override
    public abstract void valueNull(String key);

    @Override
    public abstract void newKey(String key);
    
    @Override
    public abstract void handleEvent(JSONEvent event) throws JSONEventParserException;

    public String getDataValue(JSONEvent event) {
        String dataVal = null;
        if (event.getData() != null) {

            try {
                final String val = new String(event.getData().array(), this.getParserSettings().getCharset());
                //dataVal = decoder.decode(event.getData()).toString();
                //dataVal = new String(event.getData().array());
                dataVal = val;
            } catch (final Exception e) {
                throw new JSONEventParserException(e);
            }
            

        }

        return dataVal;
    }

}
