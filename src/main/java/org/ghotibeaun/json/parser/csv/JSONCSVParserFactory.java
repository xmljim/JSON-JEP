package org.ghotibeaun.json.parser.csv;

public abstract class JSONCSVParserFactory {

    public static JSONCSVParser newJSONCSVParser(CSVSettings settings) {
        JSONCSVParserImpl csvParserImpl = new JSONCSVParserImpl(settings);
        return csvParserImpl;
    }
}
