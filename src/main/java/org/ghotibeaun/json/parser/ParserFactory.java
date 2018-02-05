package org.ghotibeaun.json.parser;

import org.ghotibeaun.json.exception.JSONParserException;
import org.ghotibeaun.json.factory.FactorySettings;

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

}
