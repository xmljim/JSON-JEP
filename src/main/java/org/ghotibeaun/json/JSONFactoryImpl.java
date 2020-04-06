package org.ghotibeaun.json;

import org.ghotibeaun.json.exception.JSONParserException;
import org.ghotibeaun.json.exception.JSONSerializationException;
import org.ghotibeaun.json.parser.JSONParser;
import org.ghotibeaun.json.parser.ParserFactory;
import org.ghotibeaun.json.parser.csv.CSVSettings;
import org.ghotibeaun.json.parser.csv.JSONCSVParser;
import org.ghotibeaun.json.serializer.JSONSerializer;
import org.ghotibeaun.json.serializer.SerializationFactory;

class JSONFactoryImpl extends JSONFactory {

    public JSONFactoryImpl() {
    }

    @Override
    public JSONParser newParser() throws JSONParserException {
        return ParserFactory.getParser();
    }

    @Override
    public JSONSerializer newSerializer() throws JSONSerializationException {
        return SerializationFactory.getSerializer();
    }
    
    @Override
    public JSONCSVParser newCsvParser(CSVSettings settings) {
        return ParserFactory.getCsvParser(settings);
    }
    
    @Override
    public JSONCSVParser newCsvParser() {
        return ParserFactory.getCsvParser(CSVSettings.getDefaultSettings());
    }

}
