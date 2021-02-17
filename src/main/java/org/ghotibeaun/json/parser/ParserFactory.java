package org.ghotibeaun.json.parser;

import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.parser.csv.CSVSettings;
import org.ghotibeaun.json.parser.csv.JSONCSVParser;
import org.ghotibeaun.json.parser.csv.JSONCSVParserFactory;

public class ParserFactory {

    private ParserFactory() {
        // private to avoid instantiation
    }

    public static JSONParser getParser() {
        return FactorySettings.createFactoryClass(FactorySettings.JSON_PARSER_CLASS);
    }

    public static JSONCSVParser getCsvParser(CSVSettings settings) {
        return JSONCSVParserFactory.newJSONCSVParser(settings);
    }

}
