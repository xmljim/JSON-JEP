package org.ghotibeaun.json.parser;

import org.ghotibeaun.json.exception.JSONParserException;
import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.parser.csv.CSVSettings;
import org.ghotibeaun.json.parser.csv.JSONCSVParser;
import org.ghotibeaun.json.parser.csv.JSONCSVParserFactory;

public class ParserFactory {

    private ParserFactory() {
        // private to avoid instantiation
    }

    @SuppressWarnings("unchecked")
    public static JSONParser getParser() {
        JSONParser parser = null;
        try {
            final Class<JSONParser> clazz = (Class<JSONParser>) ParserFactory.class.getClassLoader().loadClass(FactorySettings.getSetting(FactorySettings.JSON_PARSER));
            parser = clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new JSONParserException(e);
        }

        return parser;
    }
    
    public static JSONCSVParser getCsvParser(CSVSettings settings) {
        return JSONCSVParserFactory.newJSONCSVParser(settings);
    }

}
