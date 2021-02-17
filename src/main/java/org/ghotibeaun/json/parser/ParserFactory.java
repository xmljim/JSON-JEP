package org.ghotibeaun.json.parser;

import org.ghotibeaun.json.parser.csv.CSVSettings;
import org.ghotibeaun.json.parser.csv.JSONCSVParser;
import org.ghotibeaun.json.parser.csv.JSONCSVParserFactory;

public class ParserFactory {

    private ParserFactory() {
        // private to avoid instantiation
    }

    public static JSONParser getParser() {
        final JSONParser parser = new JSONParserImpl();

        return parser;
    }

    public static JSONCSVParser getCsvParser(CSVSettings settings) {
        return JSONCSVParserFactory.newJSONCSVParser(settings);
    }

}
